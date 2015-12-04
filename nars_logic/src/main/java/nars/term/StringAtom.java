package nars.term;

import nars.Op;
import nars.util.utf8.Utf8;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;


public abstract class StringAtom extends Atomic {

    private final String id;

    public StringAtom(String id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public abstract Op op();

    @Override
    public abstract int structure();



    @Override
    public void append(final Appendable w, final boolean pretty) throws IOException {
        Utf8.fromUtf8ToAppendable(bytes(), w);
    }

    /** preferably use toCharSequence if needing a CharSequence; it avoids a duplication */
    public StringBuilder toStringBuilder(final boolean pretty) {
        StringBuilder sb = new StringBuilder();
        sb.append(id);
        return sb;
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public boolean equals(final Object x) {
        if (this == x) return true;

        if (x instanceof StringAtom) {
            StringAtom ax = (StringAtom)x;
            return id.equals(ax.id);
        }
        return false;
    }

    /**
     * @param that The Term to be compared with the current Term
     */
    @Override
    public final int compareTo(final Object that) {
        if (that==this) return 0;

        Term t = (Term)that;
        int d = Integer.compare(op().ordinal(), t.op().ordinal());
        if (d!=0) return d;

        //if (that instanceof StringAtomic) {
            //if the op is the same, it will be a subclass of atom
            //which should have an ordering determined by its byte[]
            return id.compareTo(((StringAtom)that).id);
        //}

    }

    @Override public int getByteLen() {
        return bytes().length;
    }

    @Override public abstract boolean hasVar();

    @Override public abstract int vars();

    @Override public abstract boolean hasVarIndep();

    @Override public abstract boolean hasVarDep();

    @Override public abstract boolean hasVarQuery();

    @Override public abstract int complexity();


    @Override public int volume() { return 1; }


    @Override public abstract byte[] bytes();


    @Override
    public abstract int varIndep();

    @Override
    public abstract int varDep();

    @Override
    public abstract int varQuery();


    @Override
    public final void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(id);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        throw new RuntimeException("unimpl");
    }

}
