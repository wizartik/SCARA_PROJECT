package tests.draw;

import calc.contouringControl.Path;
import com.robotcontrol.calc.contouringControl.GCode.entities.Point;
import exc.BoundsViolation;
import exc.ImpossibleToImplement;
import exc.WrongExtension;
import exc.WrongInputData;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class BasicOpsTest extends Application {

    static ArrayList<Point> arrayList;

    public static void main(String[] args) {
        Path path = null;

        try {
            path = new Path(new File("D:\\GCodes\\cartman.ngc"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WrongExtension wrongExtension) {
            wrongExtension.printStackTrace();
        } catch (WrongInputData wrongInputData) {
            wrongInputData.printStackTrace();
        } catch (ImpossibleToImplement impossibleToImplement) {
        } catch (BoundsViolation boundsViolation) {
            boundsViolation.printStackTrace();
        }

        arrayList = path.getFullPath();

        launch(args);


    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Drawing Operations Test");
        Group root = new Group();
        Canvas canvas = new Canvas(800, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawShapes(gc);
        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    private void drawShapes(GraphicsContext gc) {
        System.out.println(arrayList.size());
        for (int i = 0; i < arrayList.size(); i+=3) {
            gc.fillOval(arrayList.get(i).getPosition()[0] * 5 + 300, arrayList
                    .get(i)
                    .getPosition()[1] * 5 + 100, 2, 2);
        }

    }
}