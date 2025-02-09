import request from "@/utils/request"
export const getMessagesByIds=(senderId,recipientId)=>{
    return request.get("/messages",{
        params:{
            senderId,
            recipientId
        }
    });
}