package nars.process;

import nars.NAR;
import nars.bag.BagBudget;
import nars.concept.Concept;
import nars.task.Task;
import nars.term.Termed;
import nars.term.compile.TermIndex;

import java.util.function.Consumer;

/**
 * Created by me on 8/5/15.
 */
public class ConceptTaskTermLinkProcess extends ConceptProcess {

    protected final BagBudget<Termed> termLink;

    public static int fireAll(NAR nar, Concept concept, BagBudget<Task> taskLink, BagBudget<Termed> termLink, Consumer<ConceptProcess> cp) {


        int[] beliefAttempts = new int[1];

        Task belief;

        Concept beliefConcept = nar.concept(termLink.get());
        if (beliefConcept != null) {
            Task task = taskLink.get();

            belief = beliefConcept.getBeliefs().top(task, nar.time());

            if (belief != null) {
                TermIndex.match(task, belief, nar, beliefResolved -> {
                    beliefAttempts[0]++;
                    cp.accept(new ConceptTaskTermLinkProcess(
                            nar, concept,
                            taskLink, termLink,
                            beliefResolved));
                });
            }

        } else {
            belief = null;
        }

        if (beliefAttempts[0] == 0) {
            //belief = null
            cp.accept(new ConceptTaskTermLinkProcess(nar, concept,
                    taskLink, termLink, belief));
            return 1;
        }

        return beliefAttempts[0];

    }


    public ConceptTaskTermLinkProcess(NAR nar, Concept concept, BagBudget<Task> taskLink, BagBudget<Termed> termLink, Task belief) {
        super(nar, concept, taskLink);

        this.termLink = termLink;




        //belief can be null:
        if (belief!=null)
            updateBelief(belief);

    }


    /**
     * @return the current termLink aka BeliefLink
     */
    @Override
    public final BagBudget<Termed> getTermLink() {
        return termLink;
    }



//    /**
//     * the current termlink / belieflink's concept
//     */
//    public Concept getTermLinkConcept() {
//        final TermLink tl = getTermLink();
//        if (tl != null) {
//            return concept(tl.getTerm());
//        }
//        return null;
//    }

    @Override
    public String toString() {
        return new StringBuilder().append(
                getClass().getSimpleName())
                .append('[').append(getConcept()).append(',')
                            .append(getTaskLink()).append(',')
                            .append(getTermLink()).append(',')
                            .append(getBelief())
                .append(']')
                .toString();
    }

}
