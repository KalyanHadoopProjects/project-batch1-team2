package com.eventproces.mapreduce.usecase5.images;

import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class KalyanImageOutputFormat extends FileOutputFormat<Object, KalyanImageWritable> {
	TaskAttemptContext job;

	@Override
	public RecordWriter<Object, KalyanImageWritable> getRecordWriter(TaskAttemptContext job) throws IOException, InterruptedException {
		this.job = job;
		return new ImageToJpegRecordWriter(job);
	}

	public Path extracted(TaskAttemptContext job, String path) throws IOException {
		return getDefaultWorkFile(job, path);
	}
}

class ImageToJpegRecordWriter extends RecordWriter<Object, KalyanImageWritable> {
	TaskAttemptContext job;
	int i = 0;
	FileSystem fs;

	ImageToJpegRecordWriter(TaskAttemptContext job) {
		this.job = job;
	}

	@Override
	public void close(TaskAttemptContext context) throws IOException {
	}

	public String nameGenerate() {
		i++;
		return "" + i;
	}

	@Override
	public synchronized void write(Object key, KalyanImageWritable value) throws IOException, InterruptedException {
		Configuration conf = job.getConfiguration();
		KalyanImageOutputFormat ios = new KalyanImageOutputFormat();
		Path file = ios.extracted(job, nameGenerate());
		FileSystem fs = file.getFileSystem(conf);
		FSDataOutputStream fileOut = fs.create(file, false);
		writeImage(value, fileOut);
	}

	public void writeImage(Object o, FSDataOutputStream out) throws IOException {
		if (o instanceof KalyanImageWritable) {
			KalyanImageWritable image = (KalyanImageWritable) o;
			ImageIO.write(image.buffer, "jpeg", (OutputStream) out);
		}
	}

}









