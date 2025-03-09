

const WS_PORT = '8090'
const BASE_URL = 'localhost'
const Ws_ADDRESS = `ws://${BASE_URL}:${WS_PORT}`



const useWebSocket = (handleMessage) => {
    const ws = new WebSocket(Ws_ADDRESS)

    //初始化函数
    const init = () => {
        bindEvent()
    }
    //绑定事件函数
    const bindEvent = () => {
        ws.addEventListener('open', handleOpen, false)
        ws.addEventListener('close', handleClose, false)
        ws.addEventListener('error', handleError, false)
        ws.addEventListener('message', handleMessage, false)
    }

    init()//启动webSocket初始化函数

    // 使用函数声明来避免 ReferenceError
    function handleOpen(e) {
        const userId = sessionStorage.getItem("userId");
        const registerMessage = {
            msgContent: null,
            msgId: null,
            msgType: 5,
            received: null,
            recipientId: null,
            senderId: userId,
            createTime: null
        };
        ws.send(JSON.stringify(registerMessage));
        console.log('open', e);
    }

    function handleClose(e) {
        console.log('close', e);
    }

    function handleError(e) {
        console.log('error', e);
    }


    return ws
}




export default useWebSocket;