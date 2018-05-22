package ru.greg3d.asserts;

import org.testng.asserts.IAssert;
import org.testng.asserts.SoftAssert;
import org.testng.collections.Maps;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by SBT-Konovalov-GV on 16.04.2018.
 */
public class SoftAssertExtended extends SoftAssert {
    private final Map<AssertionError, IAssert<?>> m_errors = Maps.newLinkedHashMap();

    protected void clearErrorMap(){
        this.m_errors.clear();
    }

    protected void doAssert(IAssert<?> a) {
        this.onBeforeAssert(a);

        try {
            a.doAssert();
            this.onAssertSuccess(a);
        } catch (AssertionError var6) {
            this.onAssertFailure(a, var6);
            this.m_errors.put(var6, a);
        } finally {
            this.onAfterAssert(a);
        }

    }

    public void assertAll() throws AssertionError{
        if(!this.m_errors.isEmpty()) {
            StringBuilder sb = new StringBuilder("The following asserts failed:");
            boolean first = true;
            Iterator var3 = this.m_errors.entrySet().iterator();

            while(var3.hasNext()) {
                Map.Entry ae = (Map.Entry)var3.next();
                if(first) {
                    first = false;
                } else {
                    sb.append(",");
                }

                sb.append("\n\t");
                sb.append(((AssertionError)ae.getKey()).getMessage());
            }

            throw new AssertionError(sb.toString());
        }
    }
}
