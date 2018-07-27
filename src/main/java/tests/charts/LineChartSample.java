package tests.charts;

import com.robotcontrol.calc.contouringControl.entities.Point;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.ArrayList;


public class LineChartSample extends Application {
    private static ArrayList<Point> path;
    private static int step;

    @Override public void start(Stage primaryStage) {
        primaryStage.setTitle("Test all");
        Button btn = new Button();
        btn.setText("test");
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                drawAll();
            }
        });

        StackPane root = new StackPane();
        root.getChildren().add(btn);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();




    }

    static private void drawAll(){
        ArrayList<Double[]> realPath = new ArrayList<>();

        ArrayList<Double[]> vxT = new ArrayList<>();
        ArrayList<Double[]> vyT = new ArrayList<>();
        ArrayList<Double[]> vzT = new ArrayList<>();

        ArrayList<Double[]> xT = new ArrayList<>();
        ArrayList<Double[]> yT = new ArrayList<>();
        ArrayList<Double[]> zT = new ArrayList<>();

        ArrayList<Double[]> T1T = new ArrayList<>();
        ArrayList<Double[]> T2T = new ArrayList<>();
        ArrayList<Double[]> T3T = new ArrayList<>();

        ArrayList<Double[]> w1T = new ArrayList<>();
        ArrayList<Double[]> w2T = new ArrayList<>();
        ArrayList<Double[]> w3T = new ArrayList<>();

        ArrayList<Double[]> vT = new ArrayList<>();
        ArrayList<Double[]> aT = new ArrayList<>();

        for (int i = 0; i < path.size() ; i += step) {
            double time = path.get(i).getTime() / 1000_000;

            double x = path.get(i).getPosition()[0];
            double y = path.get(i).getPosition()[1];
            double z = path.get(i).getPosition()[2];

            double vx = path.get(i).getAxisVelocities()[0];
            double vy = path.get(i).getAxisVelocities()[1];
            double vz = path.get(i).getAxisVelocities()[2];

            double T1 = path.get(i).getAngles()[0];
            double T2 = path.get(i).getAngles()[1];
            double T3 = path.get(i).getAngles()[2];

            double w1 = path.get(i).getAngularVelocities()[0];
            double w2 = path.get(i).getAngularVelocities()[1];
            double w3 = path.get(i).getAngularVelocities()[2];

            double v = path.get(i).getVelocity();
            double a = path.get(i).getAcceleration();

            realPath.add(new Double[]{x, y});

            vxT.add(new Double[]{time, vx});
            vyT.add(new Double[]{time, vy});
            vzT.add(new Double[]{time, vz});

            xT.add(new Double[]{time, x});
            yT.add(new Double[]{time, y});
            zT.add(new Double[]{time, z});

            T1T.add(new Double[]{time, T1});
            T2T.add(new Double[]{time, T2});
            T3T.add(new Double[]{time, T3});

            w1T.add(new Double[]{time, w1});
            w2T.add(new Double[]{time, w2});
            w3T.add(new Double[]{time, w3});

            vT.add(new Double[]{time, v});
            aT.add(new Double[]{time, a});

            if (i + step >= path.size()) break;
        }

        LineChartSample.draw(w3T, "time", "w3");
        LineChartSample.draw(w2T, "time", "w2");
        LineChartSample.draw(w1T, "time", "w1");

        LineChartSample.draw(T3T, "time", "T3");
        LineChartSample.draw(T2T, "time", "T2");
        LineChartSample.draw(T1T, "time", "T1");

        LineChartSample.draw(vzT, "time", "vz");
        LineChartSample.draw(vyT, "time", "vy");
        LineChartSample.draw(vxT, "time", "vx");

        LineChartSample.draw(aT, "time", "a");
        LineChartSample.draw(vT, "time", "v");

        LineChartSample.draw(zT, "time", "z");
        LineChartSample.draw(yT, "time", "y");
        LineChartSample.draw(xT, "time", "x");

        LineChartSample.draw(realPath, "x", "y");
    }

    static private void draw( ArrayList<Double[]> XY, String xTitle, String yTitle){
        Stage stage = new Stage();
        stage.setTitle(yTitle);
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel(xTitle);
        //creating the chart
        final LineChart<Number,Number> lineChart =
                new LineChart<Number,Number>(xAxis,yAxis);

        lineChart.setTitle(yTitle);
        //defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName("My portfolio");
        //populating the series with data


        for (int i = 0; i < XY.size(); i++) {
            double x = XY.get(i)[0];
            double y = XY.get(i)[1];

            series.getData().add(new XYChart.Data(x, y));
        }

        Scene scene  = new Scene(lineChart,800,600);
        lineChart.getData().add(series);

        stage.setScene(scene);
        stage.show();
    }

//    public static void main(String[] args) {
//        launch();
//    }

    public static void testAll(ArrayList<Point> path1, int step1){
        path = path1;
        step = step1;
        launch();
    }


}