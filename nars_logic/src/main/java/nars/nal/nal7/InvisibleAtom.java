package nars.nal.nal7;

import nars.Op;
import nars.term.AbstractStringAtom;
import nars.term.Term;
import nars.term.transform.Substitution;

/**
 * Atom which is invisible to most if not all reasoner
 * processes, useful for placeholders and intermediate
 * representations.
 *
 */
public class InvisibleAtom extends AbstractStringAtom {

    private static final byte[] empty = new byte[0];


    public InvisibleAtom(String id) {
        super(id);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }



    @Override
    public int varIndep() {
        return 0;
    }

    @Override
    public int varDep() {
        return 0;
    }

    @Override
    public int varQuery() {
        return 0;
    }

    @Override
    public byte[] bytes() {
        return empty;
    }


    @Override
    public final int structure() { return 0;     }

    @Override
    public Op op() {
        return Op.NONE;
    }

    @Override
    public final Term substituted(Substitution s) {
        return this;
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public int vars() {
        return 0;
    }


    @Override
    public int complexity() {
        return 0;
    }

    @Override
    public int volume() {
        return 0;
    }

}
