package nars.rover.run;

import automenta.vivisect.Video;
import nars.Global;
import nars.NAR;
import nars.gui.NARSwing;
import nars.model.impl.Default;
import nars.nal.task.filter.ConstantDerivationLeak;
import nars.rover.RoverEngine;
import nars.rover.robot.RoverModel;

import javax.swing.*;

/**
 * Created by me on 6/20/15.
 */
public class SomeRovers {

    static {
        Video.themeInvert();
    }

    public static void main(String[] args) {
        Global.DEBUG = false;
        Global.EXIT_ON_EXCEPTION = true;


        float fps = 25;
        boolean cpanels = true;

        final RoverEngine game = new RoverEngine();


        int rovers = 1;

        for (int i = 0; i < rovers; i++)  {

            NAR nar;
            nar = new NAR(new Default() {

                protected void initDerivationFilters() {
                    final float DERIVATION_PRIORITY_LEAK=0.4f; //https://groups.google.com/forum/#!topic/open-nars/y0XDrs2dTVs
                    final float DERIVATION_DURABILITY_LEAK=0.4f; //https://groups.google.com/forum/#!topic/open-nars/y0XDrs2dTVs
                    getLogicPolicy().derivationFilters.add(new ConstantDerivationLeak(DERIVATION_PRIORITY_LEAK, DERIVATION_DURABILITY_LEAK));
                }

            }.simulationTime().
                    setActiveConcepts(1200) );
            nar.param.inputsMaxPerCycle.set(32);
            nar.param.conceptsFiredPerCycle.set(48);

            nar.setCyclesPerFrame(4);
            (nar.param).duration.set(5*4);

            //nar.param.shortTermMemoryHistory.set(3);

            (nar.param).outputVolume.set(3);
            //nar.param.budgetThreshold.set(0.02);
            //nar.param.confidenceThreshold.set(0.02);

            /*
            (nar.param).conceptForgetDurations.set(15f);
            (nar.param).taskLinkForgetDurations.set(10f);
            (nar.param).termLinkForgetDurations.set(10f);
            (nar.param).novelTaskForgetDurations.set(10f);
            */



            game.add(new RoverModel("r" + i, nar, game));

            if (cpanels) {
                SwingUtilities.invokeLater(() -> {
                    new NARSwing(nar, false);
                });
            }
        }

        game.run(fps);
    }
}
