$(document).ready(function () {
    getJobHandlerName();
});

function getJobHandlerName() {
    $.get("executor/monitor/overview/jobHandlerNames").success(function (data) {
        showWokerPool(data);
    });
}

function showWokerPool(data) {
    $.each(data, function (i, e) {
        $.get('executor/monitor/overview/' + e).success(function (workerPoolInfo) {
            var poolInfo = e + '：正在运行' + workerPoolInfo.currentActive
                + '：正在等待 ' + (workerPoolInfo.jobSize - workerPoolInfo.completedJobCount - workerPoolInfo.currentActive)
                + '：总任务 ' + workerPoolInfo.jobSize
                + '：当前总线程 ' + workerPoolInfo.workerSize
                + '：已完成 ' + workerPoolInfo.completedJobCount;
            $('<p></p>').data('jobHandlerName', e).click(showTaskInfo).append(poolInfo).appendTo('.workerpool');
        });
    });
}

function showTaskInfo() {
    var jobHandlerName = $(this).data('jobHandlerName');
    $.get('executor/monitor/listTaskInfoAllExsist/' + jobHandlerName).success(function (data) {
        console.log(data)
        var $target = $('.taskInfo').empty();
        if (data.length < 1) {
            $('<p></p>').append("<strong>没有任务记录</strong>").appendTo($target);
            return;
        }
        $.each(data, function (i, e) {
            var taskInfo = '任务名称:' + e.jobNameHander +'_' + e.jobId + '('+e.jobInfo['JOB_NAME']+')'
                + '：状态' + (e.isDone ? '已完成' : (e.isCancel ? '取消' : '运行'))
                + '：提交时间：' + date2str(new Date(e.callTime),"yyyy-MM-dd hh:mm:ss") + '：<strong>运行参数</strong>'+e.jobInfo['EXECUTOR_PARAMS'];
            $('<p></p>').append(taskInfo).appendTo($target);
        });
    });
}

/// <summary>
///	格式化显示日期时间
/// </summary>
/// <param name="x">待显示的日期时间，例如new Date()</param>
/// <param name="y">需要显示的格式，例如yyyy-MM-dd hh:mm:ss</param>
function date2str(x,y) {
    var z = {M:x.getMonth()+1,d:x.getDate(),h:x.getHours(),m:x.getMinutes(),s:x.getSeconds()};
    y = y.replace(/(M+|d+|h+|m+|s+)/g,function(v) {return ((v.length>1?"0":"")+eval('z.'+v.slice(-1))).slice(-2)});
    return y.replace(/(y+)/g,function(v) {return x.getFullYear().toString().slice(-v.length)});
}