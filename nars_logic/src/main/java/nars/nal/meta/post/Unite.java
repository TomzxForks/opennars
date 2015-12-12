package nars.nal.meta.post;

import nars.nal.RuleMatch;
import nars.nal.meta.pre.PreCondition3Output;
import nars.term.Term;
import nars.term.Terms;
import nars.term.compound.Compound;
import nars.term.variable.Variable;

import java.util.Set;

import static nars.term.compound.GenericCompound.COMPOUND;

/**
 * Created by me on 8/15/15.
 */
public class Unite extends PreCondition3Output {

    public Unite(Term arg1, Term arg2, Term arg3) {
        super(arg1, arg2, arg3);
    }

    @Override
    public boolean test(RuleMatch m, Term a, Term b, Term c) {

        if (Unite.invalid(a,b,c)) return false;

        //ok both are extensional sets or intensional sets, build difference
        return createSetAndAddToSubstitutes(m, a, c,
                    Terms.concat(
                        ((Compound)a).terms(),
                        ((Compound)b).terms()
                    ));
    }

    public static boolean createSetAndAddToSubstitutes(RuleMatch m, Term a, Term c, Set<Term> terms) {
        if (terms.isEmpty()) return false;

        return createSetAndAddToSubstitutes(m, a, c,
                terms.toArray(new Term[terms.size()]) );
    }

    public static boolean createSetAndAddToSubstitutes(RuleMatch m, Term a, Term c, Term[] termsArray) {

        m.secondary.put(
            (Variable)c, COMPOUND(a.op(), termsArray)
        );

        return true;
    }

    public static boolean invalid(Term a, Term b, Term c) {
        return c==null || !a.op().isSet() || a.op()!=b.op();
    }


}
