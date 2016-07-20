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
        var $table = $('<table></table>').addClass('table').addClass('table-hover').append('<thead><tr><th>任务名称</th><th>任务标识</th><th>状态</th><th>提交时间</th><th>运行参数</th></tr></thead>');
        var $tbody = $('<tbody></tbody>');
        $.each(data, function (i, e) {
            var $tr = $('<tr></tr>').append($('<td></td>').append(e.jobInfo['JOB_NAME']))
                .append($('<td></td>').append(e.jobNameHander + '_' + e.jobId))
                .append($('<td></td>').append(e.done ? '完成' : (e.cancel ? '取消' : '运行')))
                .append($('<td></td>').append(date2str(new Date(e.callTime), "yyyy-MM-dd hh:mm:ss")))
                .append($('<td></td>').append(e.jobInfo['EXECUTOR_PARAMS']))
                .appendTo($tbody);
            $tbody.append($tbody);
        });
        $table.append($tbody).appendTo($target);
    });
}

/// <summary>
///	格式化显示日期时间
/// </summary>
/// <param name="x">待显示的日期时间，例如new Date()</param>
/// <param name="y">需要显示的格式，例如yyyy-MM-dd hh:mm:ss</param>
function date2str(x, y) {
    var z = {M: x.getMonth() + 1, d: x.getDate(), h: x.getHours(), m: x.getMinutes(), s: x.getSeconds()};
    y = y.replace(/(M+|d+|h+|m+|s+)/g, function (v) {
        return ((v.length > 1 ? "0" : "") + eval('z.' + v.slice(-1))).slice(-2)
    });
    return y.replace(/(y+)/g, function (v) {
        return x.getFullYear().toString().slice(-v.length)
    });
}