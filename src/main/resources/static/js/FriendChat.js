$('#btn-chat').click(function(){
    var sender = $('#name').val();
    if(sender === "") {
        showalert("Enter a user name ", "alert-danger");
        return;
    }
    var receiver = $('#btn-receiver').val();
    if(receiver === "") {
        showalert("Enter a receiver name ", "alert-danger");
        return;
    }
    $.ajax({
      type: "POST",
      url: "message/send/" + sender + "/" + receiver,
      data: $('#btn-input').val(),
      error: function(){
          showalert("Failed to send message ", "alert-danger");
       }
    });

});

$('#recommend').click(function(){
    var sender = $('#name').val();
    if(sender === "") {
        showalert("Enter a user name ", "alert-danger");
        return;
    }
    $.get("message/recommend/" + sender, function(data, status) {
        showalert("Your best friend is " + data, "alert-info");
    });
});

var lastMessageTime = null;
var lastSender = null;
setInterval(function(){
    var sender = $('#name').val();
    if(sender == "")
        return;
    if(sender == lastSender){

        $.get("message/receiveRecent/" + sender, function(data, status) {
            var i = 0;
            while(i < data.length && lastMessageTime != null && compareTime(lastMessageTime, data[i].time))
                i++;
            while(i < data.length) {
                $('#chatBox').append('<li class="left clearfix"><div class="chat-body clearfix"><div class="header">' +
                                     '<strong class="primary-font">' + data[i].sender + ' to ' + data[i].receiver +
                                     '</strong></div><p>' + data[i].content + '</p></div></li>');
                lastMessageTime = data[i].time;
                i++;
            }
        });
    } else {
        showalert("fetching all messages for " + sender, "alert-info");
        lastMessageTime = null;
        lastSender = sender;
        $('#chatBox li').remove();
        $.get("message/receive/" + sender, function(data, status) {
            for(i = 0; i < data.length; i++) {
                $('#chatBox').append('<li class="left clearfix"><div class="chat-body clearfix"><div class="header">' +
                                     '<strong class="primary-font">' + data[i].sender + ' to ' + data[i].receiver +
                                     '</strong></div><p>' + data[i].content + '</p></div></li>');
                lastMessageTime = data[i].time;
            }
        });

    }
}, 200);

var lastTimeOut = -1;
var showalert = function(message, alerttype) {
    $('#alert_placeholder div').remove();
    $('#alert_placeholder').append('<div id="alertdiv" class="alert ' +  alerttype + '">'+message+'</div>')
    if(lastTimeOut != -1)
        clearTimeout(lastTimeOut);
    lastTimeOut = setTimeout(function() { // this will automatically close the alert and remove this if the users doesnt close it in 5 secs
        $("#alertdiv").remove();
    }, 10000);
  }

var compareTime = function(time1, time2) {
    if(time1.year > time2.year)
        return true;
    if(time1.year < time2.year)
        return false;
    if(time1.dayOfYear > time2.dayOfYear)
        return true;
    if(time1.dayOfYear < time2.dayOfYear)
        return false;
    if(time1.hour > time2.hour)
        return true;
    if(time1.hour < time2.hour)
        return false;
    if(time1.minute > time2.minute)
        return true;
    if(time1.minute < time2.minute)
        return false;
    if(time1.second > time2.second)
        return true;
    if(time1.second < time2.second)
        return false;
    if(time1.nano > time2.nano)
        return true;
    if(time1.nano < time2.nano)
        return false;
    return true;
}



