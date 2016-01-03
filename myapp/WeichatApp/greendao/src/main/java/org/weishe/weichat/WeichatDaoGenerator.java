package org.weishe.weichat;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class WeichatDaoGenerator {
    public static void main(String[] args) throws Exception {
        //org.weishe.weichat为执行过本程序后生成的java文件所存放的package
        Schema schema = new Schema(1, "org.weishe.weichat");

        // addNote(schema);
        // addCustomerOrder(schema);

        addChatMessage(schema);
        addTodo(schema);
        addAttachment(schema);
        //weichat/app/src/main/java/gen 为生成java所存放的目录
        new DaoGenerator().generateAll(schema, "../Weichat/app/src/main/java-gen");
    }

    private static void addAttachment(Schema schema) {
        Entity attachment = schema.addEntity("Attachment");
        attachment.addIdProperty();
        attachment.addIntProperty("attachmentId");
        attachment.addStringProperty("name");
        attachment.addStringProperty("groupName");
        attachment.addStringProperty("path");
        attachment.addIntProperty("type");
        attachment.addLongProperty("size");
        attachment.addDateProperty("createDate");
    }

    private static void addTodo(Schema schema) {
        Entity todo = schema.addEntity("Todo");
        todo.addIdProperty();
        todo.addIntProperty("todoId");
        todo.addIntProperty("whoId");
        todo.addBooleanProperty("checked");
        todo.addIntProperty("type");
        todo.addIntProperty("fromId");
        todo.addIntProperty("groupId");
        todo.addDateProperty("createDate");
        todo.addBooleanProperty("complete");
        todo.addBooleanProperty("agree");
        todo.addStringProperty("requestMsg");
        todo.addStringProperty("handleMsg");
        todo.addStringProperty("handleDate");
        todo.addStringProperty("todoSubject");
    }

    private static void addChatMessage(Schema schema) {
        Entity chatMessage = schema.addEntity("ChatMessage");
        chatMessage.addIdProperty();
        chatMessage.addIntProperty("unCheckedCount");
        chatMessage.addIntProperty("chatMessageId");
        chatMessage.addStringProperty("content");
        chatMessage.addIntProperty("fromId");
        chatMessage.addIntProperty("toId");
        chatMessage.addDateProperty("date");
        chatMessage.addIntProperty("type");
        chatMessage.addIntProperty("msgType");
        chatMessage.addIntProperty("chatGroupId");
        chatMessage.addIntProperty("discussionGroupId");
        chatMessage.addIntProperty("whoId");
        chatMessage.addBooleanProperty("checked");
        chatMessage.addBooleanProperty("transfer");
        chatMessage.addLongProperty("attachmentId");
        chatMessage.addIntProperty("contentType");
        chatMessage.addStringProperty("fileGroupName");
        chatMessage.addStringProperty("filePath");
        chatMessage.addStringProperty("uuid");
        chatMessage.addIntProperty("status");
    }
}
