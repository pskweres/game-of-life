package pdd;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.lib.input.LineRecordReader;

import org.apache.giraph.graph.Vertex;
import org.apache.giraph.edge.Edge;
import org.apache.giraph.edge.EdgeFactory;
import org.apache.giraph.io.VertexReader;
import org.apache.giraph.io.VertexInputFormat;
import org.apache.giraph.io.formats.TextVertexInputFormat;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.io.IOException;

public class VertexStateInputFormat extends TextVertexInputFormat<LongWritable, VertexStateWritable, DoubleWritable> {

    private static final Pattern SEPARATOR = Pattern.compile("[\t ]");

    @Override
    public TextVertexReader createVertexReader(InputSplit split, TaskAttemptContext context) throws IOException {
        return new VertexStateVertexReader();
    }

    public class VertexStateVertexReader extends TextVertexReaderFromEachLineProcessed<String[]> {
        private LongWritable id;

        @Override
        protected String[] preprocessLine(Text line) throws IOException {
            String[] tokens = SEPARATOR.split(line.toString());
            id = new LongWritable(Long.parseLong(tokens[0]));
            return tokens;
        }

        @Override
        protected LongWritable getId(String[] tokens) throws IOException {
            return id;
        }

        @Override
        protected VertexStateWritable getValue(String[] tokens) throws IOException {
            return new VertexStateWritable(Double.parseDouble(tokens[1]));
        }

        @Override
        protected Iterable<Edge<LongWritable, DoubleWritable>> getEdges(String[] tokens) throws IOException {
            List<Edge<LongWritable, DoubleWritable>> edges = new ArrayList<Edge<LongWritable, DoubleWritable>>();
            for (int n = 2; n < tokens.length; n += 2) {
                LongWritable targetVertex = new LongWritable(Long.parseLong(tokens[n]));
                DoubleWritable edgeValue = new DoubleWritable(Double.parseDouble(tokens[n + 1]));
                edges.add(EdgeFactory.create(targetVertex, edgeValue));
            }
            return edges;
        }
    }
}