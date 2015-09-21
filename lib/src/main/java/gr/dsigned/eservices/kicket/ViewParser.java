package gr.dsigned.eservices.kicket;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class ViewParser implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(ViewParser.class);
    private static final QName WICKET_ID = new QName("http://wicket.apache.org/dtds.data/wicket-xhtml1.4-strict.dtd", "id", "wicket");
    private final List<Component> componentRoots = new ArrayList<Component>();
    private final Deque<TagNameAndContainerHolder> containers = new LinkedList<TagNameAndContainerHolder>();

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void createWicketComponent(String id, StartElement element) {
        Component c;
        final String modelAttribute = getModelAttribute(element);
        switch (TAG.fromTag(element.getName().getLocalPart())) {
            case SELECT:
                c = new DropDownChoice(id);
                setModel(c, modelAttribute);
                break;
            case INPUT:
                c = new TextField(id);
                setModel(c, modelAttribute);
                break;
            case FORM:
                c = new EventPublishingForm(id);
                setModel(c, modelAttribute);
                break;
            case LINK:
                c = new BookmarkablePageLink(id, PageScanner.instance()
                                                            .getPage(element.getAttributeByName(QName.valueOf("linkTo"))
                                                                            .getValue()));
                break;
            case OTHER:
            default:
                c = new Label(id);
                setModel(c, modelAttribute);
        }
        log.debug("Created component: {} of type {}", c, c.getClass());
        if (containers.size() > 0) {
            containers.peek().container.add(c);
        } else {
            componentRoots.add(c);
        }
        if (c instanceof MarkupContainer) {
            containers.push(new TagNameAndContainerHolder(element.getName()
                                                                 .getLocalPart(), (MarkupContainer) c));
        }
    }

    private void setModel(Component c, String modelAttibute) {
        if (modelAttibute != null) {
            c.setDefaultModel(new Model<Serializable>(modelAttibute));
        }
    }

    private String getModelAttribute(StartElement element) {
        final Attribute modelAttribute = element.getAttributeByName(QName.valueOf("modelAttribute"));
        return modelAttribute == null ? null : modelAttribute
            .getValue();
    }

    public List<Component> getComponentRoots() {
        return componentRoots;
    }

    public void scan(InputStream in) {
        try {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();
                if (event.isStartElement()) {
                    StartElement startElement = event.asStartElement();
                    final Attribute attribute = startElement.getAttributeByName(WICKET_ID);
                    if (attribute != null) {
                        log.debug("Found wicket element: {}", attribute.getName());
                        createWicketComponent(attribute.getValue(), startElement);
                    }
                } else if (event.isEndElement()) {
                    final EndElement endElement = event.asEndElement();
                    String name = endElement.getName().getLocalPart();
                    if (containers.size() > 0 && containers.peek()
                                                           .getTagName()
                                                           .equals(name)) {
                        containers.pop();
                    }
                }
            }
        } catch (XMLStreamException e) {
            log.error(e.getMessage(), e);
            throw new IllegalStateException(e);
        }
    }

    public enum TAG {
        SELECT("select"),
        FORM("form"),
        INPUT("input"),
        LINK("a"),
        OTHER("");
        String tag;

        TAG(String tag) {
            this.tag = tag;
        }

        public static TAG fromTag(String tag) {
            for (TAG t : values()) {
                if (t.tag.equalsIgnoreCase(tag)) {
                    return t;
                }
            }
            return OTHER;
        }
    }

    @SuppressWarnings("unused")
    private static class TagNameAndContainerHolder {

        private String tagName;

        private MarkupContainer container;

        public TagNameAndContainerHolder(String tagName, MarkupContainer container) {
            this.tagName = tagName;
            this.container = container;
        }

        public MarkupContainer getContainer() {
            return container;
        }

        public void setContainer(MarkupContainer container) {
            this.container = container;
        }

        public String getTagName() {
            return tagName;
        }

        public void setTagName(String tagName) {
            this.tagName = tagName;
        }
    }
}
