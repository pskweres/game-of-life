package pdd;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.DoubleWritable;

public class VertexStateWritable implements Writable{
      
    private double magicValue;

    public VertexStateWritable() {
        this.magicValue = 0.0d;
    }

    public VertexStateWritable(double magicValue) {
        this.magicValue = magicValue;
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        DoubleWritable valueWritable = new DoubleWritable();
        valueWritable.readFields(in);
        this.magicValue = valueWritable.get();
    }

    @Override
    public void write(DataOutput out) throws IOException {
        DoubleWritable valueWritable = new DoubleWritable(this.magicValue);
        valueWritable.write(out);
    }

    public double getMagicValue() {
        return this.magicValue;
    }

    public void setMagicValue(double magicValue) {
        this.magicValue = magicValue;
    }

    public String toString() {
        return Double.toString(this.magicValue);
    }

}