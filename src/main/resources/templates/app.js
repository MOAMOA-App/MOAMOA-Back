var stompClient = null;

// 연결 상태에 따라 UI 업데이트
function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function getAuthByUserId(userId) {
  const url = `http://localhost:8080/stomptest/auth/${userId}`; // 백엔드 API 엔드포인트

  return fetch(url)
    .then(response => {
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      return response.json();
    })
    .catch(error => {
      console.error('Error fetching data:', error);
      throw error;
    });
}

// Connect 버튼을 클릭할 때 호출됨. SockJS와 STOMP를 사용하여 WebSocket 연결 설정
// YJ: 액세스토큰 받아서 여기 연결해주는식으로 해야될듯
// stompClinet.subscribe로 '/sub/{userId}'를 구독, 42번 사용자가 로그인하고, 41번 사용자가 42번 사용자에게 메시지를 전송
// 토큰에는 42번 사용자의 토큰을 넣고, 42번 사용자는 자신의 id에 맞춰서 '/sub/42'를 구독
// 이게아닌가봄...
function connect() {
    let socket = new SockJS('http://localhost:8080/ws-stomp');
    stompClient = Stomp.over(socket);

    // 토큰을 백엔드에서 받아온 후에 연결 시도
    getAuthByUserId(42).then(data => {
        const token = auth.accessToken;
        const headers = {
            Authorization: `Bearer ${token}`
        };

        stompClient.connect(headers, function (frame) {
            setConnected(true);
            console.log('Connected: ' + frame);
            stompClient.subscribe('/sub/42', function (msg) {
                console.log('구독 중', msg);
            });
        });
    }).catch(error => {
        console.error('Error fetching token:', error);
    });

//    stompClient.connect({"token" : 4321}, function (frame) {
//        setConnected(true);
//        console.log('Connected: ' + frame);
//        stompClient.subscribe('/sub/42', function (msg) {
//            console.log('구독 중', msg);
//        });
//    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
});