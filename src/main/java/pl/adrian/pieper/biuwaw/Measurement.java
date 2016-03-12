/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adrian.pieper.biuwaw;

/**
 *
 * @author Adi
 */
public class Measurement {
    private static long maxId;

    public static Measurement create() {
        Measurement measurement = new Measurement();
        measurement.value = 100;
        measurement.id = maxId++;
        return measurement;
    }
    private double value;
    private long id;

    public Measurement() {
        id = maxId++;
    }

    
    public Measurement(Measurement newMeasurement) {
        this();
        value = newMeasurement.value;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    
    
}
