shequApp.filter('DesplayFilter',function(){
    return function(currentEndDate){

        //为什么解析出来的月份比实际上要少一个月?
        var now = new Date();
        var year = now.getFullYear();
        var month = now.getMonth()+1;
        var data = now.getDate()+10;

        now.setDate(data);
        now.setMonth(month);
        var nowString = year+"年"+month+"月"+data+"日";

        //为空时 取当前时间的十天后 非空时,取截止日期后的一天与为空时比较
        if(null==currentEndDate){
            return nowString;
        }else{
            var miles = parseInt(currentEndDate)+1*24*60*60*1000;
            var time = new Date(miles);
            var timeString = time.getFullYear() +"年"+(time.getMonth()+1)+"月"+time.getDate()+"日";

            //now>currentDate
            if(now.getTime()>time.getTime()){
                return nowString;
            }else{
                return timeString;
            }

        }
    }

});