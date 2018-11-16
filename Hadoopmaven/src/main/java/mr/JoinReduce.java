package mr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Mapper.Context;

public class JoinReduce extends Reducer<Text, Text, Text, Text>{
	
	public static final String LEFT_FILENAME = "student_info.txt";
	public static final String RIGHT_FILENAME = "student_class_info.txt";
	public static final String LEFT_FILENAME_FLAG = "l";
	public static final String RIGHT_FILENAME_FLAG = "r";
	
	protected void map(Text key,Iterable<Text> values,Context context) throws
	IOException,InterruptedException{
		Iterator<Text> iterator = values.iterator();
		
		List<String> studentClassNames = new ArrayList<String>();
		String studentName="";
		while(iterator.hasNext()){
			String[] infos = iterator.next().toString().split("\t");
			//�жϸ�����¼�������ĸ��ļ����������ļ���ʽ������¼��ȡ��Ӧ����Ϣ
			
			if(infos[1].equals(LEFT_FILENAME_FLAG)){
				studentName = infos[0];
				
			}else if(infos[1].equals(RIGHT_FILENAME_FLAG)){
				studentClassNames.add(infos[0]);
				
			}
		}
		for(int i=0;i<studentClassNames.size();i++){
			context.write(new Text(studentName), new Text(studentClassNames.get(i)));
		}
	}

}
