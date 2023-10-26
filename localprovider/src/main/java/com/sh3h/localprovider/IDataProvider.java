/**
 * @author qiweiwei
 */
package com.sh3h.localprovider;

import android.content.Context;

import com.sh3h.localprovider.greendaoEntity.HistoryTask;
import com.sh3h.localprovider.greendaoEntity.MultiMedia;
import com.sh3h.localprovider.greendaoEntity.Task;
import com.sh3h.localprovider.greendaoEntity.Word;

import java.util.List;
import java.util.Map;

/**
 * IMeterReadingDataProvider
 */
public interface IDataProvider {
    boolean init(String path, Context context);

    /**
     * 保存"我的工单"
     *
     * @param taskList
     * @return
     */
    boolean saveMyTasks(int userId, List<Task> taskList);

    /**
     * delete the tasks with user id and task ids
     *
     * @param userId
     * @param taskList
     * @return
     */
    boolean deleteMyTasks(int userId, List<Task> taskList);

    boolean deleteMyTask(int userId);

    /**
     * @param task
     * @return
     */
    boolean updateMyTask(Task task);

    boolean updateMyTaskList(List<Task> taskList);

    /**
     * 根据taskId来查询
     *
     * @param taskId
     * @return
     */
    Task queryTaskByTaskId(String taskId);

    /**
     * 更新“我的工单”
     *
     * @return
     */
    boolean updateMyTask(String taskId, int taskState);

    /**
     * 更新“历史工单”上传标志
     *
     * @param historyTask
     */
    boolean updateHistoryTask(HistoryTask historyTask);

    boolean updateHistoryTaskList(List<HistoryTask> historyTaskList);

    /**
     * 分页查询"我的工单"
     *
     * @param userId userId
     * @param offset 起始位置
     * @param size   多少条
     * @return
     */
    List<Task> loadTasks(int userId, int offset, int size, int taskType,boolean isFromMyTask);

    /**
     * 分页查询"历史工单"
     *
     * @param offset
     * @param size
     * @return
     */
    List<HistoryTask> loadHistoryTasks(int userId, int offset, int size);

    /**
     * 分页查询自开单历史记录
     *
     * @param offset
     * @param size
     * @return
     */
    List<HistoryTask> getCreateSelfOrderHistory(int userId, int offset, int size);

    /**
     * 保存"历史工单"
     *
     * @param historyTask
     * @return
     */
    boolean saveHistoryTask(HistoryTask historyTask);

    /**
     * 保存自开单
     *
     * @param task
     * @return
     */
    boolean saveCreateSelfOrder(Task task);

    /**
     * 保存自开单和历史记录
     *
     * @param task
     * @param createTask
     * @return
     */
    boolean saveCreateSelfOrderAndHistory(Task task, HistoryTask createTask);

    /**
     * 保存词典信息
     *
     * @param words
     * @return
     */
    boolean saveWords(List<Word> words);

    void clearWords();

    List<Task> getTaskList(int userId, List<Integer> taskTypeList, int offset, int limit);

    /**
     * save a media
     *
     * @param multiMedia
     * @return
     */
    boolean saveMedia(MultiMedia multiMedia);

    /**
     * get the list of medias by the conditions
     *
     * @param userId
     * @param taskId
     * @param taskType
     * @param taskState
     * @param fileType
     * @return
     */
    List<MultiMedia> getMediaList(int userId, String taskId, int taskType, int taskState, int fileType);

    /**
     * get a media by id
     *
     * @param id
     * @return
     */
    MultiMedia getMedia(long id);

    /**
     * delete a media
     *
     * @param multiMedia
     * @return
     */
    boolean deleteMedia(MultiMedia multiMedia);

    /**
     * delete a media
     *
     * @param multiMedias
     * @return
     */
    boolean deleteMediaList(List<MultiMedia> multiMedias);

    /**
     * 更新多媒体TaskId
     *
     * @param loaclTaskId
     * @param serverTaskId
     * @return
     */
    boolean updateMultiMedia(String loaclTaskId, String serverTaskId);

    /**
     * 更新多媒体上传状态
     *
     * @param taskId
     * @param taskState
     * @param taskType
     * @param uploadFlag
     * @return
     */
    boolean updateMultiMedia(String taskId, int taskState, int taskType, int uploadFlag);

    boolean updateMultiMediaList(List<MultiMedia> multiMediaList);

    /**
     * 通过taskId查询工单信息
     *
     * @param taskId
     * @return
     */
    Task getCreateSelfOrderByTaskId(int userId, String taskId);

    /**
     * 自开单上传完成，更新taskId
     *
     * @param loaclTaskId  本地taskId
     * @param serverTaskId 服务器taskId
     * @param uploadFlag   上传状态
     * @return
     */
    boolean updateCreateSelfOrder(String loaclTaskId, String serverTaskId, int uploadFlag);

    /**
     * 更新自开单历史记录的taskId和上传状态
     *
     * @param loaclTaskId
     * @param serverTaskId
     * @param uploadFlag
     * @return
     */
    boolean updateCreateSelfOrderHistory(String loaclTaskId, String serverTaskId, int uploadFlag);

    /**
     * 更新自开单历史记录的taskId
     *
     * @param loaclTaskId
     * @param serverTaskId
     * @return
     */
    boolean updateCreateSelfOrderHistory(String loaclTaskId, String serverTaskId);

    /**
     * 更新taskId和上传状态
     * 更新自开单历史记录的taskId（创建记录）
     * 更新自开单历史记录的taskId和上传状态（上传记录）
     *
     * @param loaclTaskId
     * @param serverTaskId
     * @param uploadFlag
     * @return
     */
    boolean updateCreateSelfOrderAndHistory(String loaclTaskId, String serverTaskId, int uploadFlag);

    void destroy();

    void clearAllTables();

    List<Word> getFirstWords(String str);

    Map<String, List<Word>> getNextWords(String str);

    /**
     * 得到同一个taskId的未上传的HistoryTask
     *
     * @param taskId
     * @return
     */
    List<String> getNoUploadHistoryTask(String taskId);

    boolean updateHistoryTaskReply(HistoryTask historyTask);

    /**
     * 批量修改HistoryTask上传标志
     *
     * @return
     */
    boolean updateSomeHistoryTaskFlag(List<HistoryTask> historyTaskList);

    /**
     * @param userId
     * @param taskId
     * @param taskType
     * @param taskState
     * @param replyTime
     * @return
     */
    HistoryTask getHistoryTask(int userId, String taskId, int taskType, int taskState, long replyTime);

    /**
     * 查询我的工单
     *
     * @param query
     * @return
     */
    List<Task> queryMyTask(int userId, String query);

    /**
     * 更新历史工单
     *
     * @param historyTask
     * @return
     */
    boolean updateHistoryTask(Long id, HistoryTask historyTask);

    /**
     * 获取工单历史记录
     *
     * @param userId     用户ID
     * @param taskId     任务ID
     * @param uploadFlag 上传状态
     * @return
     */
    List<HistoryTask> getHistoryTasks(int userId, String taskId, int uploadFlag);

    /**
     * 获取工单历史记录
     *
     * @param userId 用户ID
     * @param taskId 任务ID
     * @return
     */
    List<HistoryTask> getHistoryTasks(int userId, String taskId);

    /**
     * 根据任务类型获得本地的历史工单
     *
     * @param userId
     * @param taskType
     * @return
     */
    List<HistoryTask> getAllHistoryTaskList(int userId, int taskType);

    /**
     * 更新处理工单（来自自开单）的任务回填的taskId(用serverTaskId替换)
     *
     * @param taskId
     * @param serverTaskId
     * @return
     */
    boolean updateTaskReplyCreateSelf(String taskId, String serverTaskId);

    /**
     * 获取来自于自开单的处理工单
     *
     * @param userId
     * @param taskId
     * @return
     */
    List<HistoryTask> getTaskHandleCreateSelf(int userId, String taskId);

    /**
     * 更新多媒体文件的文件创建时间
     *
     * @param taskId
     * @param replyTime
     * @return
     */
    boolean updateMediaListFileTime(String taskId, long replyTime);

    /**
     * 删除过期的数据
     *
     * @param userId
     * @param currentTime
     * @param intervalTime
     * @return
     */
    List<MultiMedia> deleteHistoryTask(int userId, long currentTime, long intervalTime);

    /**
     * 查询临期工单
     *
     * @param userId
     * @return
     */
    List<Task> queryDeadlineTasks(long currentTime, int userId);

    /**
     * 是否存在临期工单
     * @param userId
     * @return
     */
    boolean isExistDeadlineTasks(int userId);

    /**
     * 获得所有站点（不包括供水热线）
     * @return
     */
    List<Word> getAllStation();

    /**
     * 获得“供水热线”词语
     * @return
     */
    Word getHotlineStation();
}
