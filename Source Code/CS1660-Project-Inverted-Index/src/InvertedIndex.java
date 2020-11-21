import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class InvertedIndex {
public static class invertedIndexMapper extends Mapper<LongWritable, Text, Text, Text> {
		
		private Text word = new Text(); 
		
		@Override
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			
			// Get the name of the file 
			String fileName = ((FileSplit) context.getInputSplit()).getPath().getName();
			
			StringTokenizer itr = new StringTokenizer(value.toString());
			
			while(itr.hasMoreTokens()) {
				word.set(itr.nextToken());
				context.write(word, new Text(fileName));
			}
		}
	}
	
	
	
	public static class invertedIndexReducer extends Reducer<Text, Text, Text, Text> {
		
		@Override
		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
			HashMap map = new HashMap(); 
			int count = 0; 
			
			for (Text value:values) {
				String valueString = value.toString();
				
				// If the hashmap exists and the file name is already present, increment it by 1
				if (map != null && map.get(valueString) != null) {
					count = (int) map.get(valueString);
					map.put(valueString, ++count);
				}
				// If the file name is not present, add it to the map
				else {
					map.put(valueString, 1);
				}
			}
			
			context.write(key, new Text(map.toString()));
		}
	}
	
	
	public static void main(String[] args) throws Exception {
		
		long startTime = System.currentTimeMillis();
		
		Configuration conf = new Configuration(); 
        String[] otherArgs = new GenericOptionsParser(conf, 
                args).getRemainingArgs(); 
        
        Job job = Job.getInstance(conf, "invert index"); 
        job.setJarByClass(InvertedIndex.class); 
  
        job.setMapperClass(invertedIndexMapper.class); 
        job.setReducerClass(invertedIndexReducer.class); 
        
        job.setNumReduceTasks(1);
  
        job.setMapOutputKeyClass(Text.class); 
        job.setMapOutputValueClass(Text.class); 
  
        job.setOutputKeyClass(Text.class); 
        job.setOutputValueClass(Text.class); 
        
        int argCounter = 1;
        for (String argument : otherArgs) {
        	
        	if (argCounter != otherArgs.length) {
        		FileInputFormat.addInputPath(job, new Path(argument));
        	}
        	// If we have reached the large argument, set it as them output path 
        	else {
        		FileOutputFormat.setOutputPath(job, new Path(argument));
        	}
        	
        	argCounter++;
        }
        
        System.exit(exitWithTime(job, startTime));
	}

	private static int exitWithTime(Job inputJob, long inputStartTime) throws Exception {
		int result = inputJob.waitForCompletion(true) ? 0 : 1;
		long endTime = System.currentTimeMillis();
		System.out.println("InvertedIndex Execution Time (ms): " + (endTime - inputStartTime));	
		return result;
	}
}

