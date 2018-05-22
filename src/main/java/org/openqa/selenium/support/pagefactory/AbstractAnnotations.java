package org.openqa.selenium.support.pagefactory;

/**
 * Created by sbt-konovalov-gv on 03.04.2018.
 */
import org.openqa.selenium.By;
import org.openqa.selenium.support.*;

import java.util.HashSet;

public abstract class AbstractAnnotations {
    public AbstractAnnotations() {
    }

    public abstract By buildBy();

    public abstract boolean isLookupCached();

    protected By buildByFromFindBys(FindBys findBys) {
        this.assertValidFindBys(findBys);
        FindBy[] findByArray = findBys.value();
        By[] byArray = new By[findByArray.length];

        for(int i = 0; i < findByArray.length; ++i) {
            byArray[i] = this.buildByFromFindBy(findByArray[i]);
        }

        return new ByChained(byArray);
    }

    protected By buildBysFromFindByOneOf(FindAll findBys) {
        this.assertValidFindAll(findBys);
        FindBy[] findByArray = findBys.value();
        By[] byArray = new By[findByArray.length];

        for(int i = 0; i < findByArray.length; ++i) {
            byArray[i] = this.buildByFromFindBy(findByArray[i]);
        }

        return new ByAll(byArray);
    }

    protected By buildByFromFindBy(FindBy findBy) {
        this.assertValidFindBy(findBy);
        By ans = this.buildByFromShortFindBy(findBy);
        if(ans == null) {
            ans = this.buildByFromLongFindBy(findBy);
        }

        return ans;
    }

    protected By buildByFromLongFindBy(FindBy findBy) {
        How how = findBy.how();
        String using = findBy.using();
        switch(how.ordinal()) {
            case 1:
                return By.className(using);
            case 2:
                return By.cssSelector(using);
            case 3:
            case 10:
                return By.id(using);
            case 4:
                return new ByIdOrName(using);
            case 5:
                return By.linkText(using);
            case 6:
                return By.name(using);
            case 7:
                return By.partialLinkText(using);
            case 8:
                return By.tagName(using);
            case 9:
                return By.xpath(using);
            default:
                throw new IllegalArgumentException("Cannot determine how to locate element ");
        }
    }

    protected By buildByFromShortFindBy(FindBy findBy) {
        return !"".equals(findBy.className())?By.className(findBy.className()):(!"".equals(findBy.css())?By.cssSelector(findBy.css()):(!"".equals(findBy.id())?By.id(findBy.id()):(!"".equals(findBy.linkText())?By.linkText(findBy.linkText()):(!"".equals(findBy.name())?By.name(findBy.name()):(!"".equals(findBy.partialLinkText())?By.partialLinkText(findBy.partialLinkText()):(!"".equals(findBy.tagName())?By.tagName(findBy.tagName()):(!"".equals(findBy.xpath())?By.xpath(findBy.xpath()):null)))))));
    }

    private void assertValidFindBys(FindBys findBys) {
        FindBy[] var2;
        int var3 = (var2 = findBys.value()).length;

        for(int var4 = 0; var4 < var3; ++var4) {
            FindBy findBy = var2[var4];
            this.assertValidFindBy(findBy);
        }

    }

    private void assertValidFindAll(FindAll findBys) {
        FindBy[] var2;
        int var3 = (var2 = findBys.value()).length;

        for(int var4 = 0; var4 < var3; ++var4) {
            FindBy findBy = var2[var4];
            this.assertValidFindBy(findBy);
        }

    }

    private void assertValidFindBy(FindBy findBy) {
        if(findBy.how() != null && findBy.using() == null) {
            throw new IllegalArgumentException("If you set the \'how\' property, you must also set \'using\'");
        } else {
            HashSet finders = new HashSet();
            if(!"".equals(findBy.using())) {
                finders.add("how: " + findBy.using());
            }

            if(!"".equals(findBy.className())) {
                finders.add("class name:" + findBy.className());
            }

            if(!"".equals(findBy.css())) {
                finders.add("css:" + findBy.css());
            }

            if(!"".equals(findBy.id())) {
                finders.add("id: " + findBy.id());
            }

            if(!"".equals(findBy.linkText())) {
                finders.add("link text: " + findBy.linkText());
            }

            if(!"".equals(findBy.name())) {
                finders.add("name: " + findBy.name());
            }

            if(!"".equals(findBy.partialLinkText())) {
                finders.add("partial link text: " + findBy.partialLinkText());
            }

            if(!"".equals(findBy.tagName())) {
                finders.add("tag name: " + findBy.tagName());
            }

            if(!"".equals(findBy.xpath())) {
                finders.add("xpath: " + findBy.xpath());
            }

            if(finders.size() > 1) {
                throw new IllegalArgumentException(String.format("You must specify at most one location strategy. Number found: %d (%s)", new Object[]{Integer.valueOf(finders.size()), finders.toString()}));
            }
        }
    }
}

