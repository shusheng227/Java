package pagerank;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class PageRank {

	private static String dataPath = "hdfs://master:9000/input/pagerankData.txt";
	private static String acountLinePath = "hdfs://master:9000/output/AcountLine.txt";
	private static String prPath = "hdfs://master:9000/output/Pr.txt";
	// 统计所有链接的出链
    private static Map<String,Integer> allOutLine = new HashMap<>();
	// 统计所有链接的现有pagerank
    private static Map<String,Double> allPageRank = new HashMap<>();
	// 统计所有链接计算后的pagerank
    private static Map<String ,Double> allNextPageRank = new HashMap<>();
	// 计算rank值时的系数
	private static double d = 0.85;
	// 计算rank值时的循环次数
	private static int cycle_index = 10;

    public static void main(String[] args) {
        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFS", "hdfs://master:9000");
        //第一个MapReduce为了统计出每个页面的出链数
        //第二个MapReduce为了循环计算每个页面的PageRank值
		//run1(configuration);
        if (run1(configuration)){
            run2(configuration);
        }
    }

    /*
    输入数据格式：
    FromNodeId	ToNodeId
    FromNodeId	ToNodeId
    */
    /*
	输出数据格式：<key，1>
    */
    static class AcountOutMapper extends Mapper<Text,Text,Text,Text>{
    	// 计算每个节点的出链数
    	private final static String one = "1";
    	private final static String zero = "0";
        @Override
        protected void map(Text key, Text value, Context context) throws IOException, InterruptedException {            
            //统计出链，<outLines[0]，1>
			//输入文件格式<FromNodeId,ToNodeId>
        	context.write(key,new Text(one));
        	context.write(new Text(value.toString()),new Text(zero));
        }
    }

    static class AcountOutReducer extends Reducer<Text,Text,Text,Text>{
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            int sum = 0;
            for (Text text : values){
                sum += Integer.parseInt(text.toString());
            }
            // <key,出链数>
            context.write(key,new Text(sum+""));	
            allOutLine.put(key.toString(),sum);
            allPageRank.put(key.toString(),1.0);
        }
    }

    public static boolean run1(Configuration configuration){
        try {
			Job job = Job.getInstance(configuration);
			FileSystem hdfs = FileSystem.get(configuration);			
			job.setJobName("acountline");
			job.setJarByClass(PageRank.class);

            job.setMapperClass(AcountOutMapper.class);
            job.setReducerClass(AcountOutReducer.class);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(Text.class);
            job.setInputFormatClass(KeyValueTextInputFormat.class);

            FileInputFormat.addInputPath(job,new Path(dataPath));

			Path outPath = new Path(acountLinePath);
            if (hdfs.exists(outPath)){
            	hdfs.delete(outPath,true);
            }
            FileOutputFormat.setOutputPath(job,outPath);

            return job.waitForCompletion(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    static class PageRankMapper extends Mapper<Text,Text,Text,Text>{
        @Override
        protected void map(Text key, Text value, Context context) throws IOException, InterruptedException {
            String myUrl = key.toString();
			int outLine = allOutLine.get(myUrl);	// 计算出度
			double nowPageRank = allPageRank.get(myUrl);
			double newPageRank = nowPageRank/outLine;
			context.write(key,new Text("0"));
            context.write(new Text(value.toString()),new Text(""+newPageRank));
        }
    }

    static class PageRankReducer extends Reducer<Text,Text,Text,Text>{
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
			// 后半段求和公式的和 （PR(1)/L(1)…………PR(i)/L(i)
            double sum = 0.0;
            for (Text text : values){
                double pr = Double.valueOf(text.toString());
                sum += pr;
            }
			// 算出该页面本次的PR结果
            double nowPr = (1-d)/allPageRank.size()+d*sum;
            allNextPageRank.put(key.toString(),nowPr);
            context.write(key,new Text(""+nowPr));
        }
    }

    public static void run2(Configuration configuration){
		for (String key : allPageRank.keySet()){
            allPageRank.put(key,1.0/allPageRank.size());
            allNextPageRank.put(key,1.0/allPageRank.size());
        }
        for(int count=0;count<cycle_index;count++)
		{
			try{
				configuration.setInt("count",count+1);
                Job job = Job.getInstance(configuration);
                FileSystem fileSystem = FileSystem.get(configuration);

                job.setJobName("pagerank");
                job.setJarByClass(PageRank.class);
                job.setJobName("Pr"+count);

                job.setMapperClass(PageRankMapper.class);
                job.setReducerClass(PageRankReducer.class);
                job.setMapOutputKeyClass(Text.class);
                job.setMapOutputValueClass(Text.class);

                job.setInputFormatClass(KeyValueTextInputFormat.class);

                Path intPath = new Path(dataPath);
                FileInputFormat.addInputPath(job,intPath);
				Path outPath = new Path(prPath+count);
                if (fileSystem.exists(outPath)){
                    fileSystem.delete(outPath,true);
                }
                FileOutputFormat.setOutputPath(job,outPath);
                boolean f = job.waitForCompletion(true);
                if (f){
                    System.out.println("job执行完毕");
//                    提取本轮所有页面的PR值和上一轮作比较，
                    for (String key : allPageRank.keySet()){
                        allPageRank.put(key,allNextPageRank.get(key));
                    }
                }
			}catch(Exception e) {
                e.printStackTrace();
            }
		}
			
    }
}
