package nars.term.transform;

import nars.term.Compound;
import nars.term.Variable;
import nars.util.data.FastPutsArrayMap;

import java.util.Arrays;

/**
 * Variable normalization
 *
 * Destructive mode modifies the input Compound instance, which is
 * fine if the concept has been created and unreferenced.
 *
 * The term 'destructive' is used because it effectively destroys some
 * information - the particular labels the input has attached.
 *
 */
public class VariableNormalization implements VariableTransform {

//    final static Comparator<Map.Entry<Variable, Variable>> comp = new Comparator<Map.Entry<Variable, Variable>>() {
//        @Override
//        public int compare(Map.Entry<Variable, Variable> c1, Map.Entry<Variable, Variable> c2) {
//            return c1.getKey().compareTo(c2.getKey());
//        }
//    };

    /**
     * overridden keyEquals necessary to implement alternate variable hash/equality test for use in normalization's variable transform hashmap
     */
    static final class VariableMap extends FastPutsArrayMap<Variable, Variable> {



        public VariableMap(int initialCapacity) {
            super(initialCapacity);
        }

        @Override
        public boolean keyEquals(final Variable a, final Object ob) {
            if (a == ob) return true;
            Variable b = ((Variable) ob);
            if (!b.isScoped() || !a.isScoped())
                return false;
            return equalName(a, b);
        }

//        @Override
//        public Variable put(Variable key, Variable value) {
//            Variable removed = super.put(key, value);
//            /*if (size() > 1)
//                Collections.sort(entries, comp);*/
//            return removed;
//        }
    }


    /** for use with compounds that have exactly one variable */
    public static final VariableTransform singleVariableNormalization = new VariableTransform( ) {

        @Override public Variable apply(Compound c, Variable v, int depth) {
            return Variable.theUnscoped(v.getType(), 1);
        }
    };

    VariableMap rename = null;

    final Compound result;
    boolean renamed = false;

    public VariableNormalization(Compound target) {
        this(target, false);
    }

    public VariableNormalization(Compound target, boolean destructively) {


        /*CompoundTransform tx = target.getTotalVariables() == 1 ?
                singleVariableNormalization : this;*/

        final CompoundTransform tx = this;

        final Compound result1;

        if (destructively)
            result1 = target.transform(tx);
        else
            result1 = target.cloneTransforming(tx);

        this.result = result1;

        if (rename != null)
            rename.clear(); //assists GC
    }


    @Override
    public Variable apply(final Compound ct, final Variable v, int depth) {
        Variable vname = v;
//            if (!v.hasVarIndep() && v.isScoped()) //already scoped; ensure uniqueness?
//                vname = vname.toString() + v.getScope().name();


        VariableMap rename = this.rename;

        if (rename == null) this.rename = rename = new VariableMap(2); //lazy allocate

//        Variable vv = rename.get(vname);
//        if (vv == null) {
//            //type + id
//            vv = newVariable(v.getType(), rename.size() + 1);
//            rename.put(vname, vv);
//            renamed = !vv.name().equals(v.name());
//        }

        final VariableMap finalRename = rename;
        Variable vv = rename.computeIfAbsent(vname, _vname -> {
            //type + id
            Variable rvv = newVariable(v.getType(), finalRename.size() + 1);
            renamed = !equalName(rvv, v);
            return rvv;
        });

        return vv;
    }

    public static boolean equalName(Variable a, Variable b) {
        return Arrays.equals(a.bytes(), b.bytes());
    }

    protected Variable newVariable(final char type, int i) {
        return Variable.theUnscoped(type, i);
    }

    public boolean hasRenamed() {
        return renamed;
    }

    public Compound getResult() {
        return result;
    }
}
