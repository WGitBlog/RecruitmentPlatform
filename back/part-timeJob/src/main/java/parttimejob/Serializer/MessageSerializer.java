package parttimejob.Serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.stereotype.Component;
import parttimejob.Entity.Messages;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

//处理Message类的时间序列化器
@Component
public class MessageSerializer extends JsonSerializer<Messages> {
    @Override
    public void serialize(Messages message, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("msgId", message.getMsgId());
        gen.writeStringField("msgContent", message.getMsgContent());
        gen.writeNumberField("senderId", message.getSenderId());
        gen.writeNumberField("recipientId", message.getRecipientId());
        gen.writeNumberField("msgType", message.getMsgType());
        gen.writeNumberField("received", message.getReceived());
        // 将 LocalDateTime 转换为字符串
        if (message.getCreateTime() != null) {
            gen.writeStringField("createTime", message.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        } else {
            gen.writeNullField("createTime");
        }
        gen.writeEndObject();
    }
}