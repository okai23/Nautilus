package org.ok23.Nautilus.Entity;

import org.cloudburstmc.protocol.bedrock.data.AttributeData;

import java.util.ArrayList;
import java.util.List;

public class AttributeContainer
{
    private List<AttributeData> attributes = new ArrayList<>();

    public AttributeContainer() {}

    public void appendData(String attribute, float min, float max, float value)
    {
        AttributeData attributeObject = new AttributeData(attribute, min, max, value);

        attributes.add(attributeObject);
    }

    public List<AttributeData> getAttributes()
    {
        return attributes;
    }
}
