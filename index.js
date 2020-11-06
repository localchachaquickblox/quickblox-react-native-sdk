const NativeModules = require('react-native').NativeModules

const {
  RNQBAuthModule: Auth,
  RNQBChatModule: Chat,
  RNQBCustomObjectsModule: CustomObjects,
  RNQBFileModule: FileModule,
  RNQBNotificationEventsModule: NotificationEvents,
  RNQBPushSubscriptionsModule: PushSubscriptions,
  RNQBSettingsModule: Settings,
  RNQBUsersModule: Users,
  RNQBWebRTCModule: WebRTC
} = NativeModules

/**
 * @typedef {Object} QBSession
 * @property {string} token
 * @property {string} [expirationDate]
 */

/**
 * @typedef {Object} DIALOG_TYPE
 * @property {1} PUBLIC_CHAT
 * @property {2} GROUP_CHAT
 * @property {3} CHAT
 */

/**
 * @typedef {Object} ChatEventNames
 * @property {string} CONNECTED
 * @property {string} CONNECTION_CLOSED_ON_ERROR
 * @property {string} CONNECTION_CLOSED
 * @property {string} RECONNECTION_FAILED
 * @property {string} RECONNECTION_SUCCESSFUL
 * @property {string} RECEIVED_NEW_MESSAGE
 * @property {string} MESSAGE_DELIVERED
 * @property {string} MESSAGE_READ
 * @property {string} RECEIVED_SYSTEM_MESSAGE
 * @property {string} USER_IS_TYPING
 * @property {string} USER_STOPPED_TYPING
 */

/**
 * @typedef {Object} Dialog
 * @property {string} createdAt Date ISO string
 * @property {Object.<string, *>} [customData]
 * @property {string} id
 * @property {boolean} isJoined
 * @property {string} [lastMessage]
 * @property {number} lastMessageDateSent timestamp
 * @property {number} lastMessageUserId
 * @property {string} [name]
 * @property {number[]} [occupantsIds]
 * @property {string} [photo]
 * @property {string} roomJid
 * @property {1|2|3} type One of {@link DIALOG_TYPE} values
 * @property {number} [unreadMessagesCount]
 * @property {string} [updatedAt]
 * @property {number} userId
 */

/**
 * @typedef {Object} DialogsFilterField
 * @property {'created_at'} CREATED_AT
 * @property {'_id'} ID
 * @property {'last_message_date_sent'} LAST_MESSAGE_DATE_SENT
 * @property {'name'} NAME
 * @property {'type'} TYPE
 * @property {'updated_at'} UPDATED_AT
 */

/**
 * @typedef {Object} DialogsFilterOperator
 * @property {'all'} ALL ALL contained IN array
 * @property {'ctn'} CTN Contains substring operator
 * @property {'gt'} GT Greater Than operator
 * @property {'gte'} GTE Greater Than or Equal to operator
 * @property {'in'} IN Contained IN array operator
 * @property {'lt'} LT Less Than operator
 * @property {'lte'} LTE Less Than or Equal to operator
 * @property {'ne'} NE Not Equal to operator
 * @property {'nin'} NIN Not contained IN array
 */

/**
 * @typedef {Object} DialogsFilter
 * @property {'_id'|'created_at'|'last_message_date_sent'|'name'|'type'|'updated_at'} field One of {@link DialogsFilterField}
 * @property {'all'|'ctn'|'gt'|'gte'|'in'|'lt'|'lte'|'ne'|'nin'} [operator] One of {@link DialogsFilterOperator}
 * @property {string} value
 */

/**
 * @typedef {Object} DialogsSort
 * @property {boolean} ascending
 * @property {'last_message_date_sent'} field
 */

/**
 * @typedef {Object} DIALOGS_FILTER
 * @property {DialogsFilterField} FIELD
 * @property {DialogsFilterOperator} OPERATOR
 */

/**
 * @typedef {Object} DIALOGS_SORT
 * @property {Object} FIELD
 * @property {'last_message_date_sent'} FIELD.LAST_MESSAGE_DATE_SENT
 */

/**
 * @typedef {Object} GetDialogsResult
 * @property {Dialog[]} dialogs
 * @property {number} skip
 * @property {number} limit
 * @property {number} total
 */

/**
 * @typedef {Object} MessageAttachment
 * @property {string} [contentType]
 * @property {string} [data]
 * @property {number} [duration]
 * @property {number} [height]
 * @property {string} id Link to file ID in QuickBlox
 * @property {string} [name]
 * @property {number} [size]
 * @property {string} type audio/video/image/...
 * @property {string} [url] Link to file in Internet
 * @property {number} [width]
 */

/**
 * @typedef {Object} Message
 * @property {Array.<MessageAttachment>} attachments
 * @property {string} [body]
 * @property {number} dateSent
 * @property {boolean} delayed
 * @property {number[]} [deliveredIds]
 * @property {string} dialogId
 * @property {string} id
 * @property {boolean} markable
 * @property {Object.<string, *>} [properties]
 * @property {number[]} [readIds]
 * @property {number} [recipientId] Private chat
 * @property {number} [senderId] Private chat
 */

/**
 * @typedef {Object} MessagesFilterField
 * @property {'attachments.type'} ATTACHMENTS_TYPE
 * @property {'message'} BODY
 * @property {'date_sent'} DATE_SENT
 * @property {'_id'} ID
 * @property {'recipient_id'} RECIPIENT_ID
 * @property {'sender_id'} SENDER_ID
 * @property {'updated_at'} UPDATED_AT
 */

/**
 * @typedef {Object} MessagesFilterOperator
 * @property {'ctn'} CTN Contains substring operator
 * @property {'gt'} GT Greater Than operator
 * @property {'gte'} GTE Greater Than or Equal to operator
 * @property {'in'} IN Contained IN array operator
 * @property {'lt'} LT Less Than operator
 * @property {'lte'} LTE Less Than or Equal to operator
 * @property {'ne'} NE Not Equal to operator
 * @property {'nin'} NIN Not contained IN array
 * @property {'or'} OR OR operator
 */

/**
 * @typedef {Object} MessagesFilter
 * @property {'attachments.type'|'message'|'date_sent'|'_id'|'recipient_id'|'sender_id'|'updated_at'} field One of {@link MessagesFilterField}
 * @property {'ctn'|'gt'|'gte'|'in'|'lt'|'lte'|'ne'|'nin'|'or'} [operator] One of {@link MessagesFilterOperator}
 * @property {string} value
 */

/**
 * @typedef {Object} MessagesSort
 * @property {boolean} ascending
 * @property {'date_sent'} field
 */

/**
 * @typedef {Object} MESSAGES_FILTER
 * @property {MessagesFilterField} FIELD
 * @property {MessagesFilterOperator} OPERATOR
 */

/**
 * @typedef {Object} MESSAGES_SORT
 * @property {Object} FIELD
 * @property {'date_sent'} FIELD.DATE_SENT
 */

/**
 * @typedef {Object} GetMessagesResult
 * @property {Message[]} messages
 * @property {number} skip
 * @property {number} limit
 */

/**
 * @typedef {Object} UsersFilterField
 * @property {'created_at'} CREATED_AT
 * @property {'email'} EMAIL
 * @property {'external_user_id'} EXTERNAL_USER_ID
 * @property {'facebook_id'} FACEBOOK_ID
 * @property {'full_name'} FULL_NAME
 * @property {'id'} ID
 * @property {'last_request_at'} LAST_REQUEST_AT
 * @property {'login'} LOGIN
 * @property {'phone'} PHONE
 * @property {'twitter_id'} TWITTER_ID
 * @property {'updated_at'} UPDATED_AT
 * @property {'website'} WEBSITE
 */

/**
 * @typedef {Object} UsersFilterOperator
 * @property {'between'} BETWEEN
 * @property {'eq'} EQ
 * @property {'ge'} GE
 * @property {'gt'} GT
 * @property {'in'} IN
 * @property {'le'} LE
 * @property {'lt'} LT
 * @property {'ne'} NE
 */

/**
 * @typedef {Object} UsersFilterType
 * @property {'date'} DATE
 * @property {'number'} NUMBER
 * @property {'string'} STRING
 */

/**
 * @typedef {Object} UsersFilter
 * @property {'created_at'|'email'|'external_user_id'|'facebook_id'|'full_name'|'id'|'last_request_at'|'login'|'phone'|'twitter_id'|'updated_at'|'website'} field One of {@link UsersFilterField}
 * @property {'date'|'number'|'string'} type One of {@link UsersFilterType}
 * @property {'between'|'eq'|'ge'|'gt'|'in'|'le'|'lt'|'ne'} operator One of {@link UsersFilterOperator}
 * @property {string} value
 */

/**
 * @typedef {Object} UsersSort
 * @property {boolean} ascending
 * @property {'created_at'|'email'|'external_user_id'|'facebook_id'|'full_name'|'id'|'last_request_at'|'login'|'phone'|'twitter_id'|'updated_at'|'website'} field One of {@link UsersFilterField}
 * @property {'date'|'number'|'string'} type One of {@link UsersFilterType}
 */

/**
 * @typedef {Object} USERS_FILTER
 * @property {UsersFilterField} FIELD
 * @property {UsersFilterOperator} OPERATOR
 * @property {UsersFilterType} TYPE
 */

/**
 * @typedef {Object} USERS_SORT
 * @property {UsersFilterField} FIELD
 * @property {UsersFilterType} TYPE
 */

/**
 * @typedef {Object} QBFile
 * @property {number} id
 * @property {string} uid
 * @property {string} contentType
 * @property {string} name
 * @property {number} size
 * @property {string} [completedAt] Date string
 * @property {boolean} isPublic
 * @property {string} [lastReadAccessTime] Date string
 * @property {string} [tags]
 */

/**
 * @typedef {Object} FileEventNames
 * @property {string} FILE_UPLOAD_PROGRESS
 */

/**
 * @see {@link https://quickblox.com/developers/Users}
 * @typedef {Object} QBUser
 * @property {number} [blobId]
 * @property {string} [customData]
 * @property {string} [email]
 * @property {string} [externalId]
 * @property {string} [facebookId]
 * @property {string} [fullName]
 * @property {number} id
 * @property {string} [lastRequestAt]
 * @property {string} [login]
 * @property {string} password
 * @property {string} [phone]
 * @property {string[]} [tags]
 * @property {number} [twitterId]
 * @property {string} [website]
 */

/**
 * @typedef {Object} GetUsersResult
 * @property {QBUser[]} users
 * @property {number} page
 * @property {number} perPage
 * @property {number} total
 */

/**
 * @typedef {Object} SdkSettings
 * @property {string} appId
 * @property {string} authKey
 * @property {string} authSecret
 * @property {string} accountKey
 * @property {string} [apiEndpoint]
 * @property {string} [chatEndpoint]
 */

/**
 * @typedef {Object} PermissionLevelType
 * @property {'open'} OPEN
 * @property {'open_for_groups'} OPEN_FOR_GROUPS
 * @property {'open_for_users_ids'} OPEN_FOR_USER_IDS
 * @property {'owner'} OWNER
 */

/**
 * @typedef {Object} PermissionsLevel
 * @property {PermissionLevelType} access
 * @property {string[]} [usersIds]
 * @property {string[]} [usersGroups]
 */

/**
 * @typedef {Object} Permissions
 * @property {string} customObjectId
 * @property {PermissionsLevel} [readLevel]
 * @property {PermissionsLevel} [updateLevel]
 * @property {PermissionsLevel} [deleteLevel]
 */

/**
 * @typedef {string | number | string[] | number[]} CustomObjectFieldValue
 */

/**
 * @see {@link https://quickblox.com/developers/Custom_Objects}
 * @typedef {Object} QBCustomObject
 * @property {string} id
 * @property {string} parentId
 * @property {string} createdAt Date ISO string
 * @property {string} updatedAt Date ISO string
 * @property {string} className
 * @property {number} userId Custom object owner
 * @property {Object.<string, CustomObjectFieldValue>} fields
 * @property {Permissions} permission
 */

/**
 * @typedef {Object} CustomObjectsArrayFilterOperator
 * @property {'all'} ALL
 */

/**
 * @typedef {Object} CustomObjectsBooleanFilterOperator
 * @property {'ne'} NE
 */

/**
 * @typedef {Object} CustomObjectsNumberFilterOperator
 * @property {'gt'} GT
 * @property {'gte'} GTE
 * @property {'in'} IN
 * @property {'lt'} LT
 * @property {'lte'} LTE
 * @property {'ne'} NE
 * @property {'nin'} NIN
 * @property {'or'} OR
 */

/**
 * @typedef {Object} CustomObjectsStringFilterOperator
 * @property {'ctn'} CTN
 * @property {'in'} IN
 * @property {'ne'} NE
 * @property {'nin'} NIN
 * @property {'or'} OR
 */

/**
 * @typedef {Object} CustomObjectsFilterOperators
 * @property {CustomObjectsArrayFilterOperator} ARRAY
 * @property {CustomObjectsBooleanFilterOperator} BOOLEAN
 * @property {CustomObjectsNumberFilterOperator} FLOAT
 * @property {CustomObjectsNumberFilterOperator} INTEGER
 * @property {CustomObjectsStringFilterOperator} STRING
 */

/**
 * @typedef {Object} CustomObjectsSearchOperators
 * @property {CustomObjectsFilterOperators} FOR_TYPE
 */

/**
 * @typedef {Object} CustomObjectsFilter
 * @property {string} field
 * @property {'all'|'ctn'|'gt'|'gte'|'in'|'lt'|'lte'|'ne'|'nin'|'or'} [operator]
 * @property {string} value
 */

/**
 * @typedef {Object} CustomObjectsSort
 * @property {boolean} ascending
 * @property {string} field
 */

/**
 * @typedef {Object} CustomObjectsArrayUpdateOperator
 * @property {'add_to_set'} ADD_TO_SET Adds a value to an array only if the value is not in the array already
 * @property {'pop'} POP Removes last element from array. To remove first element value should be equal to -1.
 * @property {'push'} PUSH Appends specified values to array
 * @property {'pull'} PULL Removes specified value from array field
 * @property {'pull_all'} PULL_ALL Removes all specified values from array
 */

/**
 * @typedef {Object} CustomObjectsNumberUpdateOperator
 * @property {'inc'} INC Increment field <field_name> to specified value. Value can positive or negative (i.e. decrement operation)
 */

/**
 * @typedef {Object} CustomObjectsUpdateForTypeOperators
 * @property {CustomObjectsArrayUpdateOperator} ARRAY
 * @property {CustomObjectsNumberUpdateOperator} FLOAT
 * @property {CustomObjectsNumberUpdateOperator} INTEGER
 */

/**
 * @typedef {Object} CustomObjectsUpdateOperators
 * @property {CustomObjectsUpdateForTypeOperators} FOR_TYPE
 */

/**
 * @typedef {Object} CustomObjectFieldUpdate
 * @property {number} [index] Update an array value by index
 * @property {'add_to_set'|'inc'|'pop'|'pull_all'|'pull'|'push'} [operator] Update operator {@link CustomObjectsUpdateOperators} (*Don't fill when **index** filled*)
 * @property {'gt'|'gte'|'in'|'lt'|'lte'|'ne'|'nin'|'or'} [pullFilter] One of {@link CustomObjectsNumberFilterOperator}. Can be used when using the pull operator
 * @property {CustomObjectFieldValue} value
 */

/**
 * @typedef {Object} PushChannelType
 * @property {'apns'} APNS
 * @property {'apns_voip'} APNS_VOIP
 * @property {'email'} EMAIL
 * @property {'gcm'} GCM
 */

/**
 * @typedef {Object} Subscription
 * @property {number} id
 * @property {string} deviceToken
 * @property {string} deviceUdid
 * @property {string} devicePlatform
 * @property {'apns'|'apns_voip'|'email'|'gcm'} notificationChannel One of {@link PushChannelType}
 */

/**
 * @typedef {Object} NotificationEventType
 * @property {'fixed_date'} FIXED_DATE
 * @property {'one_shot'} ONE_SHOT
 * @property {'period_date'} PERIOD_DATE
 */

/**
 * @typedef {Object} NotificationTypeNames
 * @property {'email'} EMAIL
 * @property {'push'} PUSH
 */

/**
 * @typedef {Object} PUSH_TYPE
 * @property {1} APNS
 * @property {2} APNS_VOIP
 * @property {3} GCM
 * @property {4} MPNS
 */

/**
 * @typedef {Object} NotificationEventPeriod
 * @property {86400} DAY
 * @property {604800} WEEK
 * @property {2592000} MONTH
 * @property {31557600} YEAR
 */

/**
 * @see {@link https://quickblox.com/developers/Messages#Parameters_2}
 * @typedef {Object} NotificationEvent
 * @property {number} id
 * @property {string} [name] Event name  
 * Service information. Only for the user.
 * @property {'fixed_date'|'one_shot'|'period_date'} type One of {@link NotificationEventType}
 * @property {'email'|'push'} notificationType One of {@link NotificationTypeNames}
 * @property {1|2|3|4} [pushType] One of {@link PUSH_TYPE}  
 * Used only if notification_type is "**push**", ignored in other cases  
 * If pushType is not present - Notification will be delivered to all possible devices/platforms for specified users
 * @property {number} [date] timestamp  
 * The date of the event when it'll fire  
 * The 'date' is required if the event's 'type' is FIXED_DATE or PERIOD_DATE.
 * @property {number} [endDate] timestamp  
 * Date of completion of the event  
 * The 'endDate' can't be less than the 'date'.
 * @property {86400|604800|2592000|31557600} [period] The period of the event  
 * Allowed values are one of {@link NotificationEventPeriod}
 * The 'period' is required if the event's 'type' is PERIOD_DATE.
 * @property {number} [occuredCount] Event's occured count
 * @property {number} senderId Event's owner
 * @property {number[]} [recipientsIds]
 * @property {string[]} [recipientsTagsAny] Recipients (users) must have at LEAST ONE tag that specified in list
 * @property {string[]} [recipientsTagsAll] Recipients (users) must exactly have ONLY ALL tags that specified in list
 * @property {string[]} [recipientsTagsExclude] Recipients (users) mustn't have tags that specified in list
 * @property {Object.<string, any>} payload Event data
 */

/**
 * @typedef {Object} RTC_SESSION_TYPE
 * @property {1} VIDEO
 * @property {2} AUDIO
 */

/**
 * @typedef {Object} RTC_SESSION_STATE
 * @property {0} NEW
 * @property {1} PENDING
 * @property {2} CONNECTING
 * @property {3} CONNECTED
 * @property {4} CLOSED
 */

/**
 * @typedef {Object} RTC_PEER_CONNECTION_STATE
 * @property {0} NEW
 * @property {1} CONNECTED
 * @property {2} FAILED
 * @property {3} DISCONNECTED
 * @property {4} CLOSED
 */

/**
 * @typedef {Object} AudioOutput
 * @property {0} EARSPEAKER
 * @property {1} LOUDSPEAKER
 * @property {2} HEADPHONES only available on Android
 * @property {3} BLUETOOTH only available on Android
 */

/**
 * @typedef {Object} WebRTC_EVENT_TYPE
 * @property {'@QB/ACCEPT'} ACCEPT
 * @property {'@QB/CALL'} CALL
 * @property {'@QB/CALL_END'} CALL_END
 * @property {'@QB/HANG_UP'} HANG_UP
 * @property {'@QB/NOT_ANSWER'} NOT_ANSWER
 * @property {'@QB/PEER_CONNECTION_STATE_CHANGED'} PEER_CONNECTION_STATE_CHANGED
 * @property {'@QB/RECEIVED_VIDEO_TRACK'} RECEIVED_VIDEO_TRACK
 * @property {'@QB/REJECT'} REJECT
 */

/**
 * @typedef {Object} RTCSession
 * @property {string} id
 * @property {RTC_SESSION_TYPE} type
 * @property {RTC_SESSION_STATE} state
 * @property {number} initiatorId
 * @property {number[]} opponentsIds
 */

/** @namespace */
const auth = {
  /**
   * Create new session
   * @param {Object} params 
   * @param {string} params.token
   * @param {string|Date} params.expirationDate
   * @returns {Promise.<QBSession>}
   */
  createSession(params = {}) {
    const data = {}
    if (typeof params.token === 'string') {
      data.token = params.token
    }
    if (typeof params.expirationDate === 'string') {
      data.expirationDate = params.expirationDate
    } else if (params.expirationDate instanceof Date) {
      data.expirationDate = params.expirationDate.toISOString()
    }
    return Auth.createSession(data)
  },
  /**
   * Get current session
   * @returns {Promise.<QBSession>}
   */
  getSession() {
    return Auth.getSession()
  },
  /**
   * Login
   * @param {object} params
   * @param {string} params.login
   * @param {string} params.password
   * @returns {Promise.<{user: QBUser, session: QBSession}>}
   */
  login(params) {
    const login = params.login;
    const password = params.password;
    if (!login || !password) {
      return Promise.reject(new Error('Login and password required'))
    }
    return Auth.login({
      login: `${login}`,
      password: `${password}`
    })
  },
  /**
   * Logout
   * @returns {Promise}
   */
  logout() {
    return Auth.logout()
  }
};

/** @namespace */
const chat = {
  addListener: Chat.addListener,
  removeListeners: Chat.removeListeners,
  /**
   * Connect to Chat XMPP
   * @param {Object} params
   * @param {string} params.userId
   * @param {string} params.password
   * @returns {Promise}
   */
  connect(params) {
    const data = {}
    const userId = params.userId
    const password = params.password ? `${params.password}` : ''
    if (userId) {
      const id = parseInt(userId, 10)
      if (!isNaN(id)) {
        data.userId = id
      }
    }
    data.password = password
    return Chat.connect(data)
  },
  /**
   * Disconnect from chat
   * @returns {Promise}
   */
  disconnect() { return Chat.disconnect() },
  /**
   * Check if connected to chat
   * @returns {Promise<boolean>}
   */
  isConnected() { return Chat.isConnected() },
  /**
   * Create dialog
   * @param {Object} params
   * @param {string} params.name
   * @param {1|2|3} params.type
   * @param {number[]} params.occupantsIds
   * @returns {Promise.<Dialog>}
   * @example
   * QB.chat
   *   .createDialog(params)
   *   .then(dialog => {
   *   })
   *   .catch(e => console.warn(e.message))
   */
  createDialog(params) {
    const occupantsIds = params.occupantsIds
    const name = params.name
    const type = params.type
    const data = {}
    if (Array.isArray(occupantsIds) && occupantsIds.length) {
      const occupantsIdsNumbers = occupantsIds
        .map(id => {
          const num = parseInt(id)
          return isNaN(num) ? undefined : num
        })
        .filter(id => id)
      data.occupantsIds = occupantsIdsNumbers
    }
    if (name) {
      data.name = `${name}`
    }
    if (type && typeof type === 'number') {
      data.type = type
    }
    return Chat.createDialog(data)
  },
  /**
   * Update dialog
   * @param {Object} params
   * @param {string} params.dialogId
   * @param {number[]} [params.addUsers]
   * @param {number[]} [params.removeUsers]
   * @param {string} [params.name]
   * @returns {Promise.<Dialog>}
   * @example
   * QB.chat
   *   .updateDialog(params)
   *   .then(dialog => {
   *     // dialog updated successfully
   *   })
   *   .catch(e => console.warn(e.message))
   */
  updateDialog(params) {
    return Chat.updateDialog(params)
  },
  /**
   * Delete dialog  
   * **NOTE** only Dialog owner (creator) can delete Dialog
   * @param {Object} params
   * @param {string} params.dialogId
   * @returns {Promise.<undefined>}
   */
  deleteDialog(params) {
    const data = {}
    if (params.dialogId) {
      data.dialogId = `${params.dialogId}`
    }
    return Chat.deleteDialog(data)
  },
  /**
   * Join dialog  
   * NOTE: only for Group dialog
   * @param {Object} params
   * @param {string} params.dialogId
   * @returns {Promise.<Dialog>}
   */
  joinDialog(params) {
    const data = {}
    if (params.dialogId) {
      data.dialogId = `${params.dialogId}`
    }
    return Chat.joinDialog(data)
  },
  /**
   * Leave dialog  
   * NOTE: only for Group dialog
   * @param {Object} params
   * @param {string} params.dialogId
   * @returns {Promise}
   */
  leaveDialog(params) {
    const data = {}
    if (params.dialogId) {
      data.dialogId = `${params.dialogId}`
    }
    return Chat.leaveDialog(data)
  },
  /**
   * Returns list of users Ids which are online per dialog  
   * NOTE: only for Group dialog
   * @param {Object} params
   * @param {string} params.dialogId
   * @returns {Promise<number[]>}
   */
  getOnlineUsers(params) {
    const data = {}
    if (params.dialogId) {
      data.dialogId = `${params.dialogId}`
    }
    return Chat.getOnlineUsers(data)
  },
  /**
   * Returns list of dialogs matching search criterias
   * @param {Object} params
   * @param {DialogsFilter} [params.filter]
   * @param {DialogsSort} [params.sort]
   * @param {number} [params.limit]
   * @param {number} [params.skip]
   * @returns {Promise.<GetDialogsResult>}
   * @example
   * QB.chat
   *   .getDialogs(params)
   *   .then((response) => {
   *      response.dialogs
   *   })
   *   .catch(e => console.warn(e.message))
   */
  getDialogs(params) {
    const limit = params.limit
    const skip = params.skip
    const query = {}
    if (params.filter) {
      query.filter = params.filter
    }
    if (params.sort) {
      query.sort = params.sort
    }
    if (limit && parseInt(limit, 10)) {
      query.limit = parseInt(limit, 10)
    }
    if (skip && parseInt(skip, 10)) {
      query.skip = parseInt(skip, 10)
    }
    return Chat.getDialogs(query)
  },
  /**
   * Returns number of dialogs available for current user
   * @returns {Promise<number>}
   */
  getDialogsCount() { return Chat.getDialogsCount() },
  /**
   * Pings server
   * @returns {Promise}
   */
  pingServer() { return Chat.pingServer() },
  /**
   * Pings user by userId provided
   * @param {Object} params
   * @param {number} params.userId
   * @returns {Promise}
   */
  pingUser(params) {
    const userId = params.userId
    if (!userId) {
      return Promise.reject(new Error('User ID is required'))
    }
    if (isNaN(parseInt(userId, 10))) {
      return Promise.reject(new Error('User ID should be of type number'))
    }
    return Chat.pingUser({ userId: parseInt(userId, 10) })
  },
  /**
   * Returns list of dialog messages matching search criterias
   * @param {Object} params
   * @param {string} params.dialogId
   * @param {MessagesFilter} [params.filter]
   * @param {MessagesSort} [params.sort]
   * @param {number} [params.limit]
   * @param {number} [params.skip]
   * @param {boolean} [params.markAsRead]
   * @returns {Promise.<GetMessagesResult>}
   * @example
   * QB.chat
   *   .getDialogMessages({
   *     dialogId: params.dialogId,
   *     sort: {
   *       ascending: false,
   *       field: QB.chat.MESSAGES_SORT.FIELD.DATE_SENT
   *     },
   *     markAsRead: false
   *   })
   *   .then(({ messages }) => {
   *     // do something with messages
   *   })
   *   .catch(e => console.warn(e.message))
   */
  getDialogMessages(params) {
    const limit = params.limit
    const skip = params.skip
    const markAsRead = params.markAsRead
    const data = {}
    if (params.dialogId) {
      data.dialogId = `${params.dialogId}`
    }
    if (params.filter) {
      data.filter = params.filter
    }
    if (params.sort) {
      data.sort = params.sort
    }
    if (typeof limit === 'number') {
      data.limit = limit
    }
    if (typeof skip === 'number') {
      data.skip = skip
    }
    if (typeof markAsRead === 'boolean') {
      data.markAsRead = markAsRead
    }
    return Chat.getDialogMessages(data)
  },
  /**
   * 
   * @param {Object} params
   * @param {string} params.dialogId
   * @param {string} params.body message text
   * @param {Array} [params.attachments]
   * @param {Object} params.properties
   * @param {boolean} [params.markable] defaults to **true**
   * @param {number} [params.dateSent] timestamp
   * @param {boolean} params.saveToHistory
   * @returns {Promise}
   */
  sendMessage(params) {
    const attachments = params.attachments
    const dateSent = params.dateSent
    const markable = params.markable || true
    const saveToHistory = params.saveToHistory
    const data = {}
    if (params.dialogId) {
      data.dialogId = `${params.dialogId}`
    }
    if (params.body) {
      data.body = `${params.body}`
    }
    if (attachments && Array.isArray(attachments)) {
      data.attachments = attachments
    }
    data.properties = params.properties
    if (typeof markable === 'boolean') {
      data.markable = markable
    }
    if (typeof dateSent === 'number') {
      data.dateSent = dateSent
    } else {
      data.dateSent = Date.now()
    }
    if (typeof saveToHistory === 'boolean') {
      data.saveToHistory = saveToHistory
    }
    return Chat.sendMessage(data)
  },
  /**
   * Mark message as read
   * @param {Object} params
   * @param {Message} params.message
   * @returns {Promise}
   */
  markMessageRead(params) {
    return Chat.markMessageRead(params)
  },
  /**
  * Mark message as delivered
  * @param {Object} params
  * @param {Message} params.message
  * @returns {Promise}
  */
  markMessageDelivered(params) {
    return Chat.markMessageDelivered(params)
  },
  /**
   * Sends system message
   * @param {Object} params
   * @param {number} params.recipientId
   * @param {Object.<string, string>} params.properties Custom data to send. Example:    
   * **xmpp_room_jid**: Dialog roomJid  
   * **name**: Dialog name  
   * **dialog_id**: Dialog Id  
   * **type**: Dialog type  
   * **occupants_ids**: Comma-separated user Ids  
   * **notification_type**: 1 - dialog created, 2 - user(s) added to dialog, 3 - current user left dialog
   * @returns {Promise}
   */
  sendSystemMessage(params) {
    const recipientId = params.recipientId
    const data = {}
    if (!isNaN(parseInt(recipientId))) {
      data.recipientId = parseInt(recipientId, 10)
    }
    data.properties = params.properties
    return Chat.sendSystemMessage(data)
  },
  /**
   * Send "user is typing" event to dialog
   * @param {Object} params
   * @param {string} params.dialogId
   * @returns {Promise}
   */
  sendIsTyping(params) {
    const data = {}
    if (params.dialogId) {
      data.dialogId = `${params.dialogId}`
    }
    return Chat.sendIsTyping(data)
  },
  /**
   * Send "user stopped typing" event to dialog
   * @param {Object} params
   * @param {string} params.dialogId
   * @returns {Promise}
   */
  sendStoppedTyping(params) {
    const data = {}
    if (params.dialogId) {
      data.dialogId = `${params.dialogId}`
    }
    return Chat.sendStoppedTyping(data)
  },
  /**
   * @readonly
   * @type {ChatEventNames}
   */
  EVENT_TYPE: Chat.EVENT_TYPE,
  /**
   * @readonly
   * @type {DIALOGS_FILTER}
   */
  DIALOGS_FILTER: Chat.DIALOGS_FILTER,
  /**
   * @see {@link https://quickblox.com/developers/Chat#Retrieve_dialogs}
   * @readonly
   * @type {DIALOGS_SORT}
   */
  DIALOGS_SORT: Chat.DIALOGS_SORT,
  /**
   * @readonly
   * @type {DIALOG_TYPE}
   */
  DIALOG_TYPE: Chat.DIALOG_TYPE,
  /**
   * @see {@link https://quickblox.com/developers/Chat#Retrieve_messages}
   * @readonly
   * @type {MESSAGES_FILTER}
   */
  MESSAGES_FILTER: Chat.MESSAGES_FILTER,
  /**
   * @readonly
   * @type {MESSAGES_SORT}
   */
  MESSAGES_SORT: Chat.MESSAGES_SORT,
}

/** @namespace */
const events = {
  /**
   * Create notification event
   * @param {Object} params 
   * @param {string} [params.name]
   * @param {'fixed_date'|'one_shot'|'period_date'} params.type One of {@link NotificationEventType}
   * @param {'email'|'push'} params.notificationType One of {@link NotificationTypeNames}
   * @param {1|2|3|4} [params.pushType] One of {@link PUSH_TYPE}  
   * Used only if notification_type is "**push**", ignored in other cases  
   * If pushType is not present - Notification will be delivered to all possible devices/platforms for specified users
   * @param {number} [params.date]
   * @param {number} [params.endDate]
   * @param {86400|604800|2592000|31557600} [params.period] One of {@link NotificationEventPeriod}
   * @param {number} [params.occuredCount]
   * @param {number} params.senderId
   * @param {number[]} [params.recipientsIds]
   * @param {string[]} [params.recipientsTagsAny]
   * @param {string[]} [params.recipientsTagsAll]
   * @param {string[]} [params.recipientsTagsExclude]
   * @param {Object.<string, any>} [params.payload]
   * @param {string} [params.payload.message]
   * @returns {Promise.<NotificationEvent[]>}
   * @example
   * QB.events
   *   .create({
   *     name: 'Test Event 123',
   *     notificationType: QB.events.NOTIFICATION_TYPE.PUSH,
   *     payload: { message: 'Hey there! This is test notification event' },
   *     recipientsIds: [93732090, 93908219],
   *     senderId: auth.user.id,
   *     type: QB.events.NOTIFICATION_EVENT_TYPE.ONE_SHOT,
   *   })
   *   .then(array => {
   *     // array - event(s) created from parameters
   *   })
   *   .catch(e => console.warn(e.message))
   */
  create(params) {
    return NotificationEvents.create(params)
  },
  /**
   * Get notification events
   * @param {Object} [params] 
   * @param {number} [params.page]
   * @param {number} [params.perPage]
   * @returns {Promise.<NotificationEvent[]>}
   */
  get(params) {
    return NotificationEvents.get(params)
  },
  /**
   * Get notification event by Id
   * @param {Object} params 
   * @param {number} params.id
   * @returns {Promise.<NotificationEvent>}
   */
  getById(params) {
    return NotificationEvents.getById(params)
  },
  /**
   * Update notification event by Id
   * @param {Object} params 
   * @param {number} params.id  
   * @param {boolean} [params.active] Provides ability to re-enable one-shot event
   * @param {Object.<string, any>} [params.payload]
   * @param {number} [params.date]
   * @param {NotificationEventPeriod} [params.period]
   * @param {string} [params.name]
   * @returns {Promise.<NotificationEvent>}
   * @example
   * QB.events
   *   .update({ id: params.id, active: true })
   *   .then(event => {
   *     // do something with updated event
   *   })
   *   .catch(e => console.warn(e.message))
   */
  update(params) {
    return NotificationEvents.update(params)
  },
  /**
   * Remove notification event by Id
   * @param {Object} params 
   * @param {number} params.id
   * @returns {Promise.<undefined>}
   */
  remove(params) {
    return NotificationEvents.remove(params)
  },
  /**
   * @readonly
   * @type {NotificationEventPeriod}
   */
  NOTIFICATION_EVENT_PERIOD: NotificationEvents.NOTIFICATION_EVENT_PERIOD,
  /**
   * @readonly
   * @type {NotificationEventType}
   */
  NOTIFICATION_EVENT_TYPE: NotificationEvents.NOTIFICATION_EVENT_TYPE,
  /**
   * @readonly
   * @type {NotificationTypeNames}
   */
  NOTIFICATION_TYPE: NotificationEvents.NOTIFICATION_TYPE,
  /**
   * @readonly
   * @type {PUSH_TYPE}
   */
  PUSH_TYPE: NotificationEvents.PUSH_TYPE,
}

/** @namespace */
const objects = {
  /**
   * Create custom object
   * 
   * @param {Object} params
   * @param {string} params.className
   * @param {Object.<string, CustomObjectFieldValue>} params.fields
   * @returns {Promise.<QBCustomObject[]>}
   * @example
   * QB.objects
   *   .create({
   *     className: 'RNCustomObject2',
   *     fields: {
   *       text: 'Lorem ipsum dolor sit amen',
   *       version: randomNumber(),
   *       tags: [ '#test', '#customobjects', '#qb-reactnative'],
   *       friendsIds: [ randomNumber(), randomNumber(), randomNumber() ]
   *     }
   *   })
   *   .then(object => {
   *     // do something with created custom object
   *   })
   *   .catch(e => console.warn(e.message))
   *//**
   * Create multiple custom objects
   * 
   * @param {Object} params
   * @param {string} params.className
   * @param {Array.<Object.<string, CustomObjectFieldValue>>} params.objects
   * @returns {Promise.<QBCustomObject[]>}
   * @example
   * QB.objects
   *   .create({
   *     className: 'RNCustomObject2',
   *     objects: [{
   *       customText: 'asdfasdfasdf',
   *       tags: [ '#test', '#customobjects', '#qb-reactnative'],
   *       text: 'Lorem ipsum dolor sit amen 1',
   *       version: randomNumber()
   *     }, {
   *       friendsIds: [],
   *       tags: [ '#test', '#customobjects', '#qb-reactnative'],
   *       text: 'Lorem ipsum dolor sit amen 2',
   *       version: randomNumber(),
   *     }, {
   *       tags: [ '#test', '#customobjects', '#qb-reactnative'],
   *       text: 'Lorem ipsum dolor sit amen 3',
   *       version: randomNumber(),
   *     }]
   *   })
   *   .then(objects => {
   *     // do something with created custom objects
   *   })
   *   .catch(e => console.warn(e.message))
   */
  create(params) {
    return CustomObjects.create(params)
  },
  /**
   * Find custom objects w/o filters
   * @param {Object} params 
   * @param {string} params.className
   * @param {CustomObjectsFilter} [params.filter]
   * @param {CustomObjectsSort} [params.sort]
   * @param {number} [params.limit]
   * @param {number} [params.skip]
   * @param {string[]} [params.include] a list of fields for inclusion to the result set
   * @param {string[]} [params.exclude] a list of fields for exclusion from the result set
   * @returns {Promise.<QBCustomObject[]>}
   * @example
   * QB.objects
   *   .get({
   *     className: 'RNCustomObject2',
   *     filter: {
   *       field: 'version',
   *       operator: QB.objects.OBJECTS_SEARCH_OPERATOR.FOR_TYPE.INTEGER.GTE,
   *       value: '1000'
   *     },
   *     sort: {
   *       ascending: false,
   *       field: 'version'
   *     }
   *   })
   *   .then(objects => {
   *     // do something with received custom objects
   *   })
   *   .catch(e => console.warn(e.message))
   */
  get(params) {
    return CustomObjects.get(params)
  },
  /**
   * @param {Object} params
   * @param {string} params.className
   * @param {string[]} params.objectsIds
   * @returns {Promise.<QBCustomObject[]>}
   */
  getByIds(params) {
    return CustomObjects.getByIds(params)
  },
  /**
   * Update custom object
   * @param {Object} params 
   * @param {string} params.className
   * @param {string} params.id
   * @param {Object.<string, CustomObjectFieldValue | CustomObjectFieldUpdate>} params.fields
   * @return {Promise.<QBCustomObject>}
   * @example
   * QB.objects
   *   .update({
   *     className: 'RNCustomObject2',
   *     id: object.id,
   *     fields: {
   *       text: 'Lorem ipsum dolor sit amen UPDATED',
   *       tags: {
   *         operator: QB.objects.OBJECTS_UPDATE_OPERATOR.FOR_TYPE.ARRAY.ADD_TO_SET,
   *         value: '#update',
   *       },
   *       friendsIds: {
   *         operator: QB.objects.OBJECTS_UPDATE_OPERATOR.FOR_TYPE.ARRAY.PULL,
   *         pullFilter: QB.objects.PULL_FILTER.IN,
   *         value: user.id,
   *       },
   *       version: {
   *         operator: QB.objects.OBJECTS_UPDATE_OPERATOR.FOR_TYPE.INTEGER.INC,
   *         value: '1'
   *       }
   *     }
   *   })
   *   .then(object => {
   *     // do something with updated custom object
   *   })
   *   .catch(e => console.warn(e.message))
   *//**
   * Update multiple custom objects
   * @param {Object} params 
   * @param {string} params.className
   * @param {Array.<{ id: string, fields: Object.<string, CustomObjectFieldValue> }>} params.objects
   * @return {Promise.<QBCustomObject[]>}
   * @example
   * QB.objects
   *   .update({
   *     className: 'RNCustomObject2',
   *     objects: [{
   *       id: object1.id,
   *       fields: {
   *         text: 'Lorem ipsum dolor sit amen UPDATED',
   *         tags: [ '#update' ],
   *       },
   *     }, {
   *       id: object2.id,
   *       fields: {
   *         friendsIds: [ randomNumber(), randomNumber() ],
   *         version: randomNumber(),
   *       }
   *     }]
   *   })
   *   .then(objects => {
   *     // do something with updated custom objects
   *   })
   *   .catch(e => console.warn(e.message))
   */
  update(params) {
    return CustomObjects.update(params)
  },
  /**
   * Remove Custom Object(s)
   * @param {Object} params
   * @param {string} params.className
   * @param {string | string[]} params.ids Pass either object Id, or array with objects Ids
   * @return {Promise}
   */
  remove(params) {
    const data = {}
    data.className = params.className
    if (params.ids) {
      if (Array.isArray(params.ids)) {
        data.ids = params.ids.map(val => `${val}`)
      } else {
        data.ids = [`${params.ids}`]
      }
    }
    return CustomObjects.remove(data)
  },
  /**
   * @readonly
   * @type {CustomObjectsSearchOperators}
   * @see {@link https://quickblox.com/developers/Custom_Objects#Search_operators}
   */
  OBJECTS_SEARCH_OPERATOR: CustomObjects.OBJECTS_SEARCH_OPERATOR,
  /**
   * @readonly
   * @type {CustomObjectsUpdateOperators}
   * @see {@link https://quickblox.com/developers/Custom_Objects#Special_update_operators}
   * @see {@link https://quickblox.com/developers/SimpleSample-customObjects-android#Special_update_oparators}
   * @see {@link https://quickblox.com/developers/SimpleSample-customObjects-ios#Special_update_operators}
   */
  OBJECTS_UPDATE_OPERATOR: CustomObjects.OBJECTS_UPDATE_OPERATOR,
  /**
   * @readonly
   * @type {PermissionLevelType}
   */
  PERMISSIONS_LEVEL: CustomObjects.PERMISSIONS_LEVEL,
  /**
   * @readonly
   * @type {CustomObjectsNumberFilterOperator}
   */
  PULL_FILTER: CustomObjects.PULL_FILTER
}

/** @namespace */
const content = {
  addListener: FileModule.addListener,
  removeListeners: FileModule.removeListeners,
  /**
  * @param {Object} params
  * @param {string} params.url path to file in local filesystem
  * @param {boolean} params.public whether file should have public access
  * @returns {Promise.<QBFile>} returns uploaded File
  */
  upload(params) {
    const data = {}
    if (params.url) {
      data.url = `${params.url}`
    }
    if (typeof params.public === 'boolean') {
      data.public = params.public
    } else {
      data.public = false
    }
    return FileModule.upload(data)
  },
  /**
  * @param {Object} params
  * @param {number} params.id file Id
  * @returns {Promise.<QBFile>} returns information for File
  */
  getInfo(params) {
    return FileModule.getInfo(params)
  },
  /**
   * @param {Object} params
   * @param {string} params.uid File unique identifier
   * @returns {Promise.<string>} returns url to a file data
   */
  getPublicUrl(params) {
    return FileModule.getPublicUrl(params)
  },
  /**
   * @param {Object} params
   * @param {string} params.uid file unique identifier
   * @returns {Promise.<string>} returns url with token to a file data
   */
  getPrivateURL(params) {
    return FileModule.getPrivateURL(params)
  },
  /**
   * Subscribe to upload progress events
   * @param {Object} params
   * @param {string} params.url file path which has been passed to QBFile.upload
   * @returns {Promise}
   */
  subscribeUploadProgress(params) {
    return FileModule.subscribeUploadProgress(params)
  },
  /**
   * Unsubscribe from upload progress events
   * @param {Object} params
   * @param {string} params.url
   * @returns {Promise}
   */
  unsubscribeUploadProgress(params) {
    return FileModule.unsubscribeUploadProgress(params)
  },
  /**
   * @readonly
   * @type {FileEventNames}
   */
  EVENT_TYPE: FileModule.EVENT_TYPE
}

/** @namespace */
const subscriptions = {
  /**
   * Subscribe to push events
   * @param {Object} params
   * @param {string} params.deviceToken Token recevied upon registering at APNS or FCM server
   * @param {PushChannelType} [params.pushChannel]
   * @returns {Promise.<Subscription[]>}
   */
  create(params) {
    return PushSubscriptions.create(params)
  },
  /**
   * Get subscriptions
   * @returns {Promise.<Subscription[]>}
   */
  get() {
    return PushSubscriptions.get()
  },
  /**
   * Delete subscription by Id
   * @param {Object} params
   * @param {number} params.id
   * @returns {Promise}
   */
  remove(params) {
    return PushSubscriptions.remove(params)
  },
  /**
   * @readonly
   * @type {PushChannelType}
   */
  PUSH_CHANNEL: PushSubscriptions.PUSH_CHANNEL
}

/** @namespace */
const settings = {
  /**
   * Initialize QB SDK with parameters
   * @param {SdkSettings} params
   * @returns {Promise}
   */
  init(params) {
    const {
      appId,
      authKey,
      authSecret,
      accountKey,
      apiEndpoint,
      chatEndpoint,
    } = params
    if (!appId || !authKey || !authSecret || !accountKey) {
      return Promise.reject(new Error('Required parameter missing'))
    }
    return Settings.init({
      appId: `${appId}`,
      authKey: `${authKey}`,
      authSecret: `${authSecret}`,
      accountKey: `${accountKey}`,
      apiEndpoint: apiEndpoint ? `${apiEndpoint}` : undefined,
      chatEndpoint: chatEndpoint ? `${chatEndpoint}` : undefined,
    })
  },
  /**
   * Request Quickblox SDK configuration
   * @returns {Promise<SdkSettings>}
   */
  get() {
    return Settings.get()
  },
  /**
   * @param {Object} params
   * @param {boolean} params.autoReconnect Whether Stream Management resumption should be used if supported by the server (`false` by default)
   * @param {number} params.messageTimeout Preferred resumption time in seconds
   * @returns {Promise}
   */
  initStreamManagement(params) {
    const autoReconnect = params.autoReconnect || false
    const messageTimeout = params.messageTimeout
    return Settings.initStreamManagement({
      autoReconnect,
      messageTimeout
    })
  },
  enableCarbons() {
    return Settings.enableCarbons()
  },
  disableCarbons() {
    return Settings.disableCarbons()
  },
  /**
   * Enable / diable autoreconnect to XMPP  
   * Sets if the reconnection mechanism is allowed to be used  
   * By default reconnection is allowed.
   * @param {Object} params 
   * @param {boolean} params.enable
   */
  enableAutoReconnect(params) {
    const data = {
      enable: Boolean(params.enable)
    }
    return Settings.enableAutoReconnect(data)
  }
}

/** @namespace */
const users = {
  /**
   * Create new user
   * @param {Object} user
   * @param {number} [user.blobId]
   * @param {string} [user.customData]
   * @param {string} [user.email]
   * @param {string} [user.externalId]
   * @param {string} [user.facebookId]
   * @param {string} [user.fullName]
   * @param {string} user.login
   * @param {string} user.password
   * @param {string} [user.phone]
   * @param {string[]} [user.tags]
   * @param {string} [user.twitterId]
   * @param {string} [user.website]
   * @returns {Promise.<QBUser>}
   */
  create(user) {
    if (!user) {
      return Promise.reject(new Error('Required parameters missing'))
    }
    const userProps = {
      blobId: 'number',
      customData: 'string',
      email: 'string',
      externalId: 'number',
      facebookId: 'number',
      fullName: 'string',
      login: 'string',
      password: 'string',
      phone: 'string',
      tags: 'array',
      twitterId: 'number',
      website: 'string'
    }
    const data = {}
    for (var key in userProps) {
      if (user[key] !== undefined) {
        if (typeof user[key] !== userProps[key]) {
          if (userProps[key] === 'string') {
            data[key] = `${user[key]}`
          }
          if (userProps[key] === 'number') {
            const num = parseInt(user[key], 10)
            if (!isNaN(num)) {
              data[key] = num
            }
          }
          if (userProps[key] === 'array') {
            if (Array.isArray(user[key])) {
              data[key] = user[key]
            }
          }
        } else {
          data[key] = user[key]
        }
      }
    }
    return Users.create(data)
  },
  /**
   * Update current user
   * @param {Object} user
   * @param {number} [user.blobId]
   * @param {string} [user.customData]
   * @param {string} [user.email]
   * @param {string} [user.externalId]
   * @param {string} [user.facebookId]
   * @param {string} [user.fullName]
   * @param {string} [user.login]
   * @param {string} user.password
   * @param {string} user.newPassword
   * @param {string} [user.phone]
   * @param {string[]} [user.tags]
   * @param {string} [user.twitterId]
   * @param {string} [user.website]
   * @returns {Promise.<QBUser>}
   */
  update(user) {
    return Users.update(user)
  },
  /**
   * Get list of users
   * @param {Object} params
   * @param {UsersFilter} [params.filter]
   * @param {UsersSort} [params.sort]
   * @param {number} [params.page]
   * @param {number} [params.perPage]
   * @returns {Promise.<GetUsersResult>}
   * @example
   * getUsers({
   *    filter: {
   *      type: USERS_FILTER.TYPE.NUMBER,
   *      field: USERS_FILTER.FIELD.ID,
   *      operator: USERS_FILTER.OPERATOR.BETWEEN,
   *      value: "3,2241" //string
   *    }
   * })
   * @example
   * getUsers({
    *   sort: {
    *     ascending: true, // asc/desc
    *     type: USERS_SORT.TYPE.DATE,
    *     field: USERS_SORT.FIELD.LAST_REQUEST_AT
    *   }
    * })
   */
  getUsers(params) {
    const data = {}
    if (params.filter) {
      data.filter = params.filter
    }
    if (params.sort) {
      data.sort = params.sort
    }
    if (!isNaN(parseInt(params.page))) {
      data.page = parseInt(params.page, 10)
    }
    if (!isNaN(parseInt(params.perPage))) {
      data.perPage = parseInt(params.perPage, 10)
    }
    return Users.getUsers(data)
  },
  /**
   * @see {@link https://quickblox.com/developers/Users#Filters}
   * @readonly
   * @type {USERS_FILTER}
   */
  USERS_FILTER: Users.USERS_FILTER,
  /**
   * @readonly
   * @type {USERS_SORT}
   */
  USERS_SORT: Users.USERS_SORT
}

/** @namespace */
const webrtc = {
  addListener: WebRTC.addListener,
  removeListeners: WebRTC.removeListeners,
  /**
   * Initialize WebRTC module, make it ready to process calls
   * @returns {Promise}
   */
  init() {
    return WebRTC.init()
  },
  /**
   * Release WebRTC module when it is not needed
   * @returns {Promise}
   */
  release() {
    return WebRTC.release()
  },
  /**
   * Initiate a call
   * @param {Object} params 
   * @param {number[]} params.opponentsIds
   * @param {RTC_SESSION_TYPE} params.type
   * @param {Object.<string, string>} [params.userInfo]
   * @returns {Promise.<RTCSession>}
   */
  call(params) {
    return WebRTC.call(params)
  },
  /**
   * Accept incoming call
   * @param {Object} params 
   * @param {string} params.sessionId
   * @param {Object.<string, string>} [params.userInfo]
   * @returns {Promise.<RTCSession>}
   */
  accept(params) {
    return WebRTC.accept(params)
  },
  /**
   * Reject incoming call
   * @param {Object} params 
   * @param {string} params.sessionId
   * @param {Object.<string, string>} [params.userInfo]
   * @returns {Promise.<RTCSession>}
   */
  reject(params) {
    return WebRTC.reject(params)
  },
  /**
   * End call
   * @param {Object} params 
   * @param {string} params.sessionId
   * @param {Object.<string, string>} [params.userInfo]
   * @returns {Promise.<RTCSession>}
   */
  hangUp(params) {
    return WebRTC.hangUp(params)
  },
  /**
   * Mute / unmute audio
   * @param {Object} params 
   * @param {string} params.sessionId
   * @param {number} [params.userId] if userId is not provided - working with local media stream (turn on/off microphone)
   * @param {boolean} params.enable
   * @returns {Promise}
   */
  enableAudio(params) {
    return WebRTC.enableAudio(params)
  },
  /**
   * Turn off / on video
   * @param {Object} params 
   * @param {string} params.sessionId
   * @param {number} [params.userId] if not provided - working with local media stream (turn on/off sending local video)
   * @param {boolean} params.enable
   * @returns {Promise}
   */
  enableVideo(params) {
    return WebRTC.enableVideo(params)
  },
  /**
   * Get session if it exists
   * @param {Object} params 
   * @param {string} params.sessionId
   * @returns {Promise.<RTCSession | undefined>}
   */
  getSession(params) {
    return WebRTC.getSession(params)
  },
  /**
   * Switch audio output
   * @param {Object} params
   * @param {0|1|2|3} params.output one of AUDIO_OUTPUT values
   * @returns {Promise}
   */
  switchAudioOutput(params) {
    return WebRTC.switchAudioOutput(params)
  },
  /**
   * Switch active camera
   * @param {Object} params 
   * @param {string} params.sessionId
   * @returns {Promise}
   */
  switchCamera(params) {
    return WebRTC.switchCamera(params)
  },
  /**
   * @readonly
   * @type {AudioOutput}
   */
  AUDIO_OUTPUT: WebRTC.AUDIO_OUTPUT,
  /**
   * @readonly
   * @type {RTC_SESSION_STATE}
   */
  RTC_SESSION_STATE: WebRTC.RTC_SESSION_STATE,
  /**
   * @readonly
   * @type {RTC_SESSION_TYPE}
   */
  RTC_SESSION_TYPE: WebRTC.RTC_SESSION_TYPE,
  /**
   * @readonly
   * @type {RTC_PEER_CONNECTION_STATE}
   */
  RTC_PEER_CONNECTION_STATE: WebRTC.RTC_PEER_CONNECTION_STATE,
  /**
   * @readonly
   * @type {WebRTC_EVENT_TYPE}
   */
  EVENT_TYPE: WebRTC.EVENT_TYPE
}

module.exports.auth = auth
module.exports.chat = chat
module.exports.events = events
module.exports.content = content
module.exports.objects = objects
module.exports.settings = settings
module.exports.subscriptions = subscriptions
module.exports.users = users
module.exports.webrtc = webrtc

module.exports = {
  auth,
  chat,
  events,
  content,
  objects,
  settings,
  subscriptions,
  users,
  webrtc,
}
