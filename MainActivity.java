package com.example.es_21;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static int n = 8;
    private static int N = 1024;
    private static double W = 1100;
    private double real;
    private double imagine;
    private double[] signal = new double[N];
    private Double[] cos = new Double[N*N];
    private Double[] sin = new Double[N*N];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LineGraphSeries<DataPoint> series1 = new LineGraphSeries<>(generateSignal(signal));
        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(fourier());
        GraphView graph = findViewById(R.id.graph1);
        customizationGraph(graph, series1, -5, 6);
        graph = findViewById(R.id.graph2);
        customizationGraph(graph, series2, 0, (int)(N / 3.2));
    }

    private DataPoint[] generateSignal(double[] res) {
        double phi;
        double A;
        double x;
        DataPoint[] data = new DataPoint[N];
        Random rnd = new Random();
        for (int i = 0; i < N; i++) {
            phi = rnd.nextDouble();
            A = rnd.nextDouble();
            x = 0.0;
            for (int j = 0; j < n; j++) {
                x += A * Math.sin(W / (j + 1) * i + phi);
            }
            res[i] = x;
            data[i] = new DataPoint(i, x);
        }
        return data;
    }

    private DataPoint[] fourier() {
        DataPoint[] res = new DataPoint[N];
        for (int i = 0; i < N; i++) {
            real = imagine = 0;
            for (int j = 0; j < N; j++) {
                if(cos[i*j] == null) {
                    cos[i * j] = Math.cos(2 * Math.PI * i * j / N);
                    sin[i * j] = Math.sin(2 * Math.PI * i * j / N);
                    real += signal[j] * cos[i * j];
                    imagine -= signal[j] * sin[i * j];
                } else {
                    real += signal[j] * cos[i * j];
                    imagine -= signal[j] * sin[i * j];
                }
            }
            res[i] = new DataPoint(i, Math.sqrt(Math.pow(real, 2) + Math.pow(imagine, 2)));
        }
        return res;
    }

    private void customizationGraph(GraphView graph, LineGraphSeries line, int miny, int maxy) {
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(30);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMaxY(maxy);
        graph.getViewport().setMinY(miny);
        // enable scrolling
        graph.getViewport().setScrollable(true);

        graph.addSeries(line);
    }

}
