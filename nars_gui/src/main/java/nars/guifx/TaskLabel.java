package nars.guifx;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import nars.NAR;
import nars.task.Task;
import org.apache.commons.math3.util.Precision;


public class TaskLabel extends Label {

    private final Task task;
    private final TaskSummaryIcon summary;
    //private final NSlider slider;
    private float lastPri = -1;

    public TaskLabel(String prefix, Task task, NAR n) {
        super();

        getStylesheets().setAll();
        getStyleClass().setAll();

        this.task = task;

        StringBuilder sb = new StringBuilder();
        sb.append(prefix);

        //sb.append(task.getTerm());
        task.toString(sb, n.memory, true, false, false);

        setText(sb.toString());

        //label.getStyleClass().add("tasklabel_text");
        setMouseTransparent(true);
        //label.setCacheHint(CacheHint.SCALE);
        setPickOnBounds(false);
        //setSmooth(false);
        //setCache(true);


        int iconWidth = 30;
        int iconSpacing = 1;

        setCenterShape(false);

        summary = new TaskSummaryIcon(task, this).width(iconWidth);

//        summary.hoverProperty().addListener(c -> {
//            if (summary.isHover()) {
//                Popup p = new Popup();
//
//
//                p.getContent().add(
//                        new BorderPane(
//                                new NSlider(iconWidth, 20).set(0, 0, 1)
//                        )
//                );
//
//                p.show(TaskLabel.this, 0, 0);
//
//                p.setAutoHide(true);
//                p.setHideOnEscape(true);
//            }
//        });
        /*slider = new NSlider(iconWidth, 20).set(0, 0, 1);*/




        /*getChildren().setAll(
                summary, label
        );*/
        setGraphic(summary);


        setTextAlignment(TextAlignment.LEFT);
//        setAlignment(summary, Pos.CENTER_LEFT);
//        setAlignment(label, Pos.CENTER_LEFT);

        /*slider.setOpacity(0.5);
        slider.setBlendMode(BlendMode.HARD_LIGHT);*/
        summary.setMouseTransparent(false);


        update();

        layout();

        //label.setCache(true);
    }

    public void enablePopupClickHandler(NAR nar) {

        setOnMouseClicked(e -> {
            NARfx.newWindow(nar, task);
//            Term t = task.getTerm();
//            if (t!=null) {
////                Concept c = nar.concept(t);
////                if (c != null) {
////                    NARfx.window(nar, c);
////                }
//
//            }
        });

    }

    public TaskLabel(Task task, NAR nar) {
        this("", task, nar);
    }

    public void update() {


        float pri = task.getBudget().getPriorityIfNaNThenZero();
        if (Precision.equals(lastPri, pri, 0.025)) {
            return;
        }
        lastPri = pri;

        summary.run();

        double sc = 1.0 + 1.0 * pri;
        //setScaleX(sc);
        //setScaleY(sc);
        //setFont(NARfx.mono((pri*12+12)));


        setStyle(JFX.fontSize( ((1.0f + pri)*100.0f) ) );

        setTextFill(JFX.grayscale.get(pri*0.5+0.5));



    }
}
