package com.sh3h.hotline.service;



public enum SyncType {
    NONE,

    // download
    DOWNLOAD_ALL_TASK, // all tasks
    DOWNLOAD_WORDS,//下载词语信息

    // upload
    UPLOAD_ALL_TASK, // upload all tasks of replied
    UPLOAD_ONE_TASK, // upload one task of replied
    UPLOAD_ALL_MEDIA, // upload all media of tasks
    UPLOAD_ALL_MEDIA_OF_ONE_TASK, // upload all media of one task
    UPLOAD_ONE_MEDIA_OF_ONE_TASK, // upload one media of one task

    UPLOAD_ONE_CREATE_SELF_ORDER,//上传一条自开单
    UPLOAD_All_CREATE_SELF_ORDER,//上传所有自开单

    // query
    QUERY_TASK_WITH_CONDITION, // query the tasks with the conditions
    QUERY_DETAIL_OF_ONE_TASK, // query the details of one task

    //上传任务处理
    UPLOAD_TASK_REPLY,
    UPLOAD_HISTORY_TASKS_OF_ONE_TASK,
    UPLOAD_ALL_HISTORY_TASKS_BY_TASK_TYPE

}
