package pdd;

import org.apache.giraph.graph.BasicComputation;
import org.apache.giraph.graph.Vertex;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

import org.apache.giraph.conf.LongConfOption;
import org.apache.giraph.edge.Edge;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.log4j.Logger;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import java.util.Random;

import pdd.VertexStateWritable;

public class GameOfLifeComputation extends BasicComputation<LongWritable, VertexStateWritable, DoubleWritable, DoubleWritable> {
    
    private static int MAX_VALUE = 100;
    private static int MAGIC_NUMBER = 42;
    private static int MAX_STEPS = 200;

    @Override
    public void compute(Vertex<LongWritable, VertexStateWritable, DoubleWritable> vertex,
                        Iterable<DoubleWritable> messages) throws IOException {

        if (getSuperstep() == MAX_STEPS) {
            vertex.voteToHalt();
            return;
        }

        Random generator = new Random();
        
        if (getSuperstep() == 0) {
            double magicValue = Math.floor(generator.nextDouble() * MAX_VALUE);
            VertexStateWritable vertexState = new VertexStateWritable(magicValue);
            vertex.setValue(vertexState);
        }

        for (DoubleWritable m : messages) {
            VertexStateWritable currentState = vertex.getValue();
            double newMagicValue = (currentState.getMagicValue() + m.get()) % MAX_VALUE;
            vertex.getValue().setMagicValue(newMagicValue);
        }

        if (vertex.getValue().getMagicValue() % MAGIC_NUMBER == 0) {
            vertex.getValue().setMagicValue(0.0d);
            sendMessageToAllEdges(vertex, new DoubleWritable(1.0d));
        }

        vertex.voteToHalt();
    }
}