/// <reference types="react-native" />

export * from 'react-native'

declare class QBAuth {
  createSession(params: { token: string, expirationDate: string }): Promise<any>
  getSession(): Promise<QB.Session>
  login(params: { login: string, password: string }): Promise<{ user: QB.User, session: QB.Session }>
  logout(): Promise<undefined>
}

declare class QBChat {
  connect(params: { userId: number, password: string }): Promise<undefined>
  isConnected(): Promise<boolean>
  disconnect(): Promise<undefined>
  createDialog(params: { occupantsIds: number[], name: string, type: DIALOG_TYPE }): Promise<QB.Dialog>
  updateDialog(params: { dialogId: string, addUsers?: number[], removeUsers?: number[], name?: string }): Promise<QB.Dialog>
  joinDialog(params: { dialogId: string }): Promise<QB.Dialog>
  leaveDialog(params: { dialogId: string }): Promise<any>
  deleteDialog(params: { dialogId: string }): Promise<undefined>
  getDialogs(params?: { sort?: QB.Chat.Sort, filter?: QB.Chat.Filter, limit?: number, skip?: number }): Promise<{ dialogs: QB.Dialog[], skip: number, limit: number, total: number }>
  getDialogsCount(): Promise<number>
  getOnlineUsers(params: { dialogId: string }): Promise<any>
  getDialogMessages(params: { dialogId: string, filter?: QB.Message.Filter, sort?: QB.Message.Sort, limit?: number, skip?: number, markAsRead?: boolean }): Promise<{ messages: QB.Message[], skip: number, limit: number }>
  sendMessage(params: { dialogId: string, body: string, attachments?: QB.Attachment[], properties?: { [key: string]: any }, markable?: boolean, dateSent?: number, saveToHistory?: boolean }): Promise<any>
  sendSystemMessage(params: QB.SystemMessage): Promise<any>
  markMessageDelivered(params: { message: QB.Message }): Promise<any>
  markMessageRead(params: { message: QB.Message }): Promise<any>
  pingServer(): Promise<any>
  pingUser(params: { userId: string }): Promise<any>
  sendIsTyping(params: { dialogId: string }): Promise<undefined>
  sendStoppedTyping(params: { dialogId: string }): Promise<undefined>
  DIALOGS_FILTER: {
    FIELD: typeof QB.Chat.FIELD,
    OPERATOR: typeof QB.Chat.OPERATOR,
  }
  DIALOGS_SORT: { FIELD: Pick<typeof QB.Chat.FIELD, 'LAST_MESSAGE_DATE_SENT'> }
  DIALOG_TYPE: typeof DIALOG_TYPE
  EVENT_TYPE: QB.ChatEventNames
  MESSAGES_FILTER: {
    FIELD: typeof QB.Message.FIELD
    OPERATOR: typeof QB.Message.OPERATOR
  }
  MESSAGES_SORT: { FIELD: Pick<typeof QB.Message.FIELD, 'DATE_SENT'> }
}

declare class QBNotificationEvents {
  create(params: Pick<QB.Events.NotificationEvent, Exclude<keyof QB.Events.NotificationEvent, 'id'>>): Promise<QB.Events.NotificationEvent[]>
  get(params?: { page?: number, perPage?: number }): Promise<QB.Events.NotificationEvent[]>
  getById(params: { id: QB.Events.NotificationEvent['id'] }): Promise<QB.Events.NotificationEvent>
  update(params: { id: QB.Events.NotificationEvent['id'], active?: boolean, payload?: { [key: string]: any }, date?: number, period?: typeof QB.Events.NotificationEventPeriod, name?: string }): Promise<QB.Events.NotificationEvent>
  remove(params: { id: QB.Events.NotificationEvent['id'] }): Promise<undefined>
  NOTIFICATION_EVENT_PERIOD: typeof QB.Events.NotificationEventPeriod
  NOTIFICATION_EVENT_TYPE: typeof QB.Events.NotificationEventType
  NOTIFICATION_TYPE: typeof QB.Events.NotificationType
  PUSH_TYPE: typeof QB.Events.PUSH_TYPE
}

declare class QBCustomObjects {
  create(params: { className: string, fields: { [key: string]: QB.Objects.ObjectFieldValue } }): Promise<QB.Objects.CustomObject[]>
  create(params: { className: string, objects: Array<{ [key: string]: QB.Objects.ObjectFieldValue }> }): Promise<QB.Objects.CustomObject[]>
  get(params: { className: string, filter?: QB.Objects.Filter, sort?: QB.Objects.Sort, limit?: number, skip?: number, include?: string[], exclude?: string[] }): Promise<QB.Objects.CustomObject[]>
  getByIds(params: { className: string, objectsIds: string[] }): Promise<QB.Objects.CustomObject[]>
  update(params: { className: string, id: string, fields: Array<{ [key: string]: QB.Objects.ObjectFieldValue | QB.Objects.ObjectFieldUpdate }> }): Promise<QB.Objects.CustomObject>
  update(params: { className: string, objects: Array<{ id: string, fields: QB.Objects.ObjectFieldValue }> }): Promise<QB.Objects.CustomObject[]>
  remove(params: { className: string, ids: string | string[] }): Promise<undefined>
  OBJECTS_SEARCH_OPERATOR: QB.Objects.CustomObjectsSearchOperators
  OBJECTS_UPDATE_OPERATOR: QB.Objects.CustomObjectsUpdateOperators
  PERMISSIONS_LEVEL: typeof QB.Objects.PermissionsLevelType
  PULL_FILTER: typeof QB.Objects.NumberSearchOperators
}

declare class QBFileManager {
  upload(params: { url: string, public: boolean }): Promise<QB.File>
  getInfo(params: { id: QB.File['id'] }): Promise<QB.File>
  getPublicURL(params: { uid: string }): Promise<string>
  getPrivateURL(params: { uid: string }): Promise<string>
  subscribeUploadProgress(params: { url: string }): Promise<undefined>
  unsubscribeUploadProgress(params: { url: string }): Promise<undefined>
  EVENT_TYPE: QB.File.EventType
}

declare class QBPushSubscriptions {
  create(params: { deviceToken: string, pushChannel?: typeof QB.Subscriptions.PushChannelType }): Promise<QB.Subscriptions.Subscription[]>
  get(): Promise<QB.Subscriptions.Subscription[]>
  remove(params: { id: QB.Subscriptions.Subscription['id'] }): Promise<undefined>
  PUSH_CHANNEL: typeof QB.Subscriptions.PushChannelType
}

declare class QBSettings {
  /** Initialize QB SDK with parameters */
  init(params: SdkSettings): Promise<undefined>
  /** Request Quickblox SDK configuration */
  get(): Promise<SdkSettings>
  initStreamManagement(params: { autoReconnect: boolean, messageTimeout: number }): Promise<any>
  enableCarbons(): Promise<any>
  disableCarbons(): Promise<any>
  /** Enable / diable autoreconnect to XMPP */
  enableAutoReconnect(params: { enable: boolean }): Promise<any>
}

declare class QBUsers {
  /** Create new user */
  create(user: QB.User): Promise<QB.User>
  /** Update current user */
  update(user: QB.User & { newPassword: string }): Promise<QB.User>
  getUsers(params?: { filter?: QB.User.Filter, sort?: QB.User.Sort, page?: number, perPage?: number }): Promise<{ users: QB.User[], page: number, perPage: number, total: number }>
  USERS_FILTER: {
    FIELD: typeof QB.User.FIELD,
    OPERATOR: typeof QB.User.OPERATOR,
    TYPE: typeof QB.User.TYPE,
  }
  USERS_SORT: {
    FIELD: typeof QB.User.FIELD,
    TYPE: typeof QB.User.TYPE,
  }
}

declare class QBWebRTC {
  init(): Promise<undefined>
  release(): Promise<undefined>
  call(params: { opponentsIds: number[], type: typeof QB.WebRTC.RTC_SESSION_TYPE, userInfo?: { [key: string]: string } }): Promise<QB.WebRTC.Session>
  accept(params: { sessionId: QB.WebRTC.Session['id'], userInfo?: { [key: string]: string } }): Promise<QB.WebRTC.Session>
  reject(params: { sessionId: QB.WebRTC.Session['id'], userInfo?: { [key: string]: string } }): Promise<QB.WebRTC.Session>
  hangUp(params: { sessionId: QB.WebRTC.Session['id'], userInfo?: { [key: string]: string } }): Promise<QB.WebRTC.Session>
  enableAudio(params: { sessionId: QB.WebRTC.Session['id'], userId?: QB.User['id'], enable: boolean }): Promise<undefined>
  enableVideo(params: { sessionId: QB.WebRTC.Session['id'], userId?: QB.User['id'], enable: boolean }): Promise<undefined>
  getSession(params: { sessionId: QB.WebRTC.Session['id'] }): Promise<QB.WebRTC.Session | undefined>
  switchCamera(params: { sessionId: QB.WebRTC.Session['id'] }): Promise<undefined>
  switchAudioOutput(params: { output: typeof QB.WebRTC.AUDIO_OUTPUT }): Promise<void>
  AUDIO_OUTPUT: typeof QB.WebRTC.AUDIO_OUTPUT
  RTC_PEER_CONNECTION_STATE: typeof QB.WebRTC.RTC_PEER_CONNECTION_STATE
  RTC_SESSION_STATE: typeof QB.WebRTC.RTC_SESSION_STATE
  RTC_SESSION_TYPE: typeof QB.WebRTC.RTC_SESSION_TYPE
  EVENT_TYPE: QB.WebRTC.EventTypes
}

declare enum DIALOG_TYPE {
  PUBLIC_CHAT = 1,
  GROUP_CHAT = 2,
  CHAT = 3
}

interface SdkSettings {
  appId: string
  authKey: string
  authSecret: string
  accountKey: string
  apiEndpoint?: string
  chatEndpoint?: string
}

declare namespace QB {

  interface ChatEventNames {
    CONNECTED: string
    CONNECTION_CLOSED_ON_ERROR: string
    CONNECTION_CLOSED: string
    RECONNECTION_FAILED: string
    RECONNECTION_SUCCESSFUL: string
    RECEIVED_NEW_MESSAGE: string
    MESSAGE_DELIVERED: string
    MESSAGE_READ: string
    RECEIVED_SYSTEM_MESSAGE: string
    USER_IS_TYPING: string
    USER_STOPPED_TYPING: string
  }

  interface Attachment {
    contentType?: string
    data?: string
    duration?: number
    height?: number
    /** link to file ID in QuickBlox */
    id: string
    name?: string
    size?: number
    /** audio/video/image/... */
    type: string
    /** link to file in Internet */
    url?: string
    width?: number
  }

  interface User {
    blobId?: number
    customData?: string
    email?: string
    externalId?: string
    facebookId?: string
    fullName?: string
    id: number
    lastRequestAt?: string
    login?: string
    password: string
    phone?: string
    tags?: string[]
    twitterId?: number
    website?: string
  }

  interface Dialog {
    /** Date ISO string */
    createdAt: string
    customData?: { [key: string]: any }
    id: string
    isJoined: boolean
    lastMessage?: string
    /** timestamp */
    lastMessageDateSent: number
    lastMessageUserId: number
    name?: string
    occupantsIds?: number[]
    photo?: string
    roomJid: string
    type: DIALOG_TYPE
    unreadMessagesCount?: number
    updatedAt?: string
    userId: number
  }

  interface Message {
    attachments: Attachment[]
    body?: string
    dateSent: number
    delayed: boolean
    deliveredIds?: number[]
    dialogId: string
    id: string
    markable: boolean
    properties?: { [key: string]: any }
    readIds?: number[]
    /** Private chat */
    recipientId?: number
    /** Private chat */
    senderId?: number
  }

  interface SystemMessage {
    recipientId: User['id']
    properties: {
      /**
       * xmpp_room_jid: Dialog['roomJid']
       * name: Dialog['name']
       * dialog_id: Dialog['id']
       * type: Dialog['type']
       * occupants_ids: string // Comma-separated user Ids
       * notification_type: '1' | '2' | '3' // Allowed values are: 1, 2, 3
       * 1 - dialog created
       * 2 - user(s) added to diaalog
       * 3 - current user left dialog
       */
      [key: string]: any
    }
  }

  interface File {
    id: number
    uid: string
    contentType: string
    name: string
    size: number
    completedAt?: string // Date
    isPublic: boolean
    lastReadAccessTime?: string // Date
    tags?: string
  }

  interface Session {
    token: string
    expirationDate?: string
  }

  namespace Chat {

    enum FIELD {
      CREATED_AT = 'created_at',
      ID = '_id',
      LAST_MESSAGE_DATE_SENT = 'last_message_date_sent',
      NAME = 'name',
      TYPE = 'type',
      UPDATED_AT = 'updated_at',
    }

    enum OPERATOR {
      /** ALL contained IN array */
      ALL = 'all',
      /** Contains substring operator */
      CTN = 'ctn',
      /** Greater Than operator */
      GT = 'gt',
      /** Greater Than or Equal to operator */
      GTE = 'gte',
      /** Contained IN array operator */
      IN = 'in',
      /** Less Than operator */
      LT = 'lt',
      /** Less Than or Equal to operator */
      LTE = 'lte',
      /** Not Equal to operator */
      NE = 'ne',
      /** Not contained IN array */
      NIN = 'nin',
    }

    interface Filter {
      field: typeof FIELD
      operator?: typeof OPERATOR
      value: string
    }

    interface Sort {
      ascending: boolean
      field: FIELD.LAST_MESSAGE_DATE_SENT
    }

  }

  namespace Events {

    enum NotificationEventPeriod {
      DAY = 86400,
      WEEK = 604800,
      MONTH = 2592000,
      YEAR = 31557600,
    }

    enum NotificationEventType {
      FIXED_DATE = 'fixed_date',
      ONE_SHOT = 'one_shot',
      PERIOD_DATE = 'period_date',
    }

    enum NotificationType {
      PUSH = 'push',
      EMAIL = 'email',
    }

    enum PUSH_TYPE {
      APNS = 1,
      APNS_VOIP = 2,
      GCM = 3,
      MPNS = 4
    }

    /** https://quickblox.com/developers/Messages#Parameters_2 */
    interface NotificationEvent {
      id: number
      /**
       * Event name  
       * Service information. Only for the user.
       */
      name?: string
      type: typeof NotificationEventType
      notificationType: typeof NotificationType
      /**
       * Used only if notification_type is "**push**", ignored in other cases  
       * If pushType is not present - Notification will be delivered to all possible devices/platforms for specified users
       */
      pushType?: typeof PUSH_TYPE
      /**
       * timestamp  
       * The date of the event when it'll fire  
       * The 'date' is required if the event's 'type' is FIXED_DATE or PERIOD_DATE.
       */
      date?: number
      /**
       * timestamp  
       * Date of completion of the event  
       * The 'endDate' can't be less than the 'date'.
       */
      endDate?: number
      /**
       * The period of the event  
       * The 'period' is required if the event's 'type' is PERIOD_DATE.
       */
      period?: typeof NotificationEventPeriod
      /** Event's occured count */
      occuredCount?: number
      /** Event's owner */
      senderId: number
      recipientsIds?: number[]
      /** Recipients (users) must have at LEAST ONE tag that specified in list */
      recipientsTagsAny?: string[]
      /** Recipients (users) must exactly have ONLY ALL tags that specified in list */
      recipientsTagsAll?: string[]
      /** Recipients (users) mustn't have tags that specified in list */
      recipientsTagsExclude?: string[]
      /** Event data */
      payload?: { message?: string, [key: string]: any }
    }

  }

  namespace File {

    interface EventType {
      FILE_UPLOAD_PROGRESS: string
    }

  }

  namespace Message {

    enum FIELD {
      ATTACHMENTS_TYPE = 'attachments.type',
      BODY = 'message',
      DATE_SENT = 'date_sent',
      ID = '_id',
      RECIPIENT_ID = 'recipient_id',
      SENDER_ID = 'sender_id',
      UPDATED_AT = 'updated_at',
    }

    enum OPERATOR {
      /** Contains substring operator */
      CTN = 'ctn',
      /** Greater Than operator */
      GT = 'gt',
      /** Greater Than or Equal to operator */
      GTE = 'gte',
      /** Contained IN array operator */
      IN = 'in',
      /** Less Than operator */
      LT = 'lt',
      /** Less Than or Equal to operator */
      LTE = 'lte',
      /** Not Equal to operator */
      NE = 'ne',
      /** Not contained IN array */
      NIN = 'nin',
      /** OR operator */
      OR = 'or',
    }

    interface Filter {
      field: typeof FIELD
      operator?: typeof OPERATOR
      value: string
    }

    interface Sort {
      ascending: boolean
      field: FIELD.DATE_SENT
    }

  }

  namespace Objects {

    enum PermissionsLevelType {
      OPEN = 'open',
      OPEN_FOR_GROUPS = 'open_for_groups',
      OPEN_FOR_USER_IDS = 'open_for_users_ids',
      OWNER = 'owner'
    }

    interface PermissionsLevel {
      access: typeof PermissionsLevelType
      usersIds?: string[]
      usersGroups?: string[]
    }

    interface Permissions {
      customObjectId: string
      readLevel?: PermissionsLevel
      updateLevel?: PermissionsLevel
      deleteLevel?: PermissionsLevel
    }

    enum ArraySearchOperators {
      ALL = 'all'
    }

    enum BooleanSearchOperators {
      NE = 'ne'
    }

    enum NumberSearchOperators {
      GT = 'gt',
      GTE = 'gte',
      IN = 'in',
      LT = 'lt',
      LTE = 'lte',
      NE = 'ne',
      NIN = 'nin',
      OR = 'or',
    }

    enum StringSearchOperators {
      CTN = 'ctn',
      IN = 'in',
      NE = 'ne',
      NIN = 'nin',
      OR = 'or',
    }

    enum ArrayUpdateOperators {
      /** Adds a value to an array only if the value is not in the array already */
      ADD_TO_SET = 'add_to_set',
      /** Removes last element from array. To remove first element value should be equal to -1. */
      POP = 'pop',
      /** Appends specified values to array */
      PUSH = 'push',
      /** Removes specified value from array field */
      PULL = 'pull',
      /** Removes all specified values from array */
      PULL_ALL = 'pull_all',
    }

    enum NumberUpdateOperators {
      /** Increment field <field_name> to specified value. Value can positive or negative (i.e. decrement operation) */
      INC = 'inc'
    }

    type CustomObjectsSearchOperator =
      typeof ArraySearchOperators |
      typeof BooleanSearchOperators |
      typeof NumberSearchOperators |
      typeof StringSearchOperators

    type CustomObjectsUpdateOperator =
      typeof ArrayUpdateOperators |
      typeof NumberUpdateOperators

    type ObjectFieldValue = string | number | string[] | number[]
 
    interface ObjectFieldUpdate {
      /** Update an array value by index */
      index?: number
      /** Update operator (Don't fill when **index** in use) */
      operator?: CustomObjectsUpdateOperator
      /** Can be used when using the pull operator */
      pullFilter?: typeof NumberSearchOperators
      value: ObjectFieldValue
    }

    interface CustomObject {
      id: string
      parentId: string
      /** Date ISO string */
      createdAt: string
      /** Date ISO string */
      updatedAt: string
      className: string
      /** Custom object owner */
      userId: number
      fields: { [key: string]: ObjectFieldValue }
      permission: Permissions
    }

    interface CustomObjectsSearchOperators {
      FOR_TYPE: {
        ARRAY: typeof ArraySearchOperators
        BOOLEAN: typeof BooleanSearchOperators
        FLOAT: typeof NumberSearchOperators
        INTEGER: typeof NumberSearchOperators
        STRING: typeof StringSearchOperators
      }
    }

    interface Filter {
      field: string
      operator?: CustomObjectsSearchOperator
      value: string
    }

    interface Sort {
      ascending: boolean
      field: string
    }

    interface CustomObjectsUpdateOperators {
      FOR_TYPE: {
        ARRAY: typeof ArrayUpdateOperators
        FLOAT: typeof NumberUpdateOperators
        INTEGER: typeof NumberUpdateOperators
      }
    }

  }

  namespace Subscriptions {

    enum PushChannelType {
      APNS = 'apns',
      APNS_VOIP = 'apns_voip',
      EMAIL = 'email',
      GCM = 'gcm',
    }

    interface Subscription {
      id: number
      deviceToken: string
      deviceUdid: string
      devicePlatform: string
      notificationChannel: typeof PushChannelType
    }

  }

  namespace User {

    enum FIELD {
      CREATED_AT = 'created_at',
      EMAIL = 'email',
      EXTERNAL_USER_ID = 'external_user_id',
      FACEBOOK_ID = 'facebook_id',
      FULL_NAME = 'full_name',
      ID = 'id',
      LAST_REQUEST_AT = 'last_request_at',
      LOGIN = 'login',
      PHONE = 'phone',
      TWITTER_ID = 'twitter_id',
      UPDATED_AT = 'updated_at',
      WEBSITE = 'website',
    }

    enum OPERATOR {
      BETWEEN = 'between',
      EQ = 'eq',
      GE = 'ge',
      GT = 'gt',
      IN = 'in',
      LE = 'le',
      LT = 'lt',
      NE = 'ne',
    }

    enum TYPE {
      DATE = 'date',
      NUMBER = 'number',
      STRING = 'string',
    }

    interface Filter {
      field: typeof FIELD
      type: typeof TYPE
      operator: typeof OPERATOR
      value: string
    }

    interface Sort {
      ascending: boolean
      field: typeof FIELD
      type: typeof TYPE
    }
  }

  namespace WebRTC {

    interface EventTypes {
      ACCEPT: '@QB/ACCEPT'
      CALL: '@QB/CALL'
      CALL_END: '@QB/CALL_END'
      HANG_UP: '@QB/HANG_UP'
      NOT_ANSWER: '@QB/NOT_ANSWER'
      PEER_CONNECTION_STATE_CHANGED: '@QB/PEER_CONNECTION_STATE_CHANGED'
      RECEIVED_VIDEO_TRACK: '@QB/RECEIVED_VIDEO_TRACK'
      REJECT: '@QB/REJECT'
    }

    enum RTC_SESSION_TYPE {
      VIDEO = 1,
      AUDIO = 2,
    }

    enum RTC_SESSION_STATE {
      NEW = 0,
      PENDING = 1,
      CONNECTING = 2,
      CONNECTED = 3,
      CLOSED = 4,
    }

    enum RTC_PEER_CONNECTION_STATE {
      NEW = 0,
      CONNECTED = 1,
      FAILED = 2,
      DISCONNECTED = 3,
      CLOSED = 4,
    }

    enum AUDIO_OUTPUT {
      EARSPEAKER = 0,
      LOUDSPEAKER = 1,
      /* only available on Android */
      HEADPHONES = 2,
      /* only available on Android */
      BLUETOOTH = 3
    }

    interface Session {
      id: string
      initiatorId: number
      opponentsIds: number[]
      state: typeof RTC_SESSION_STATE
      type: typeof RTC_SESSION_TYPE
    }

  }

  export const auth: QBAuth
  export const chat: QBChat
  export const events: QBNotificationEvents
  export const content: QBFileManager
  export const objects: QBCustomObjects
  export const settings: QBSettings
  export const subscriptions: QBPushSubscriptions
  export const users: QBUsers
  export const webrtc: QBWebRTC
}

declare module 'react-native' {
  export interface NativeModulesStatic {
    RNQBAuthModule: QBAuth,
    RNQBChatModule: QBChat,
    RNQBCustomObjectsModule: QBCustomObjects,
    RNQBFileModule: QBFileManager,
    RNQBNotificationEventsModule: QBNotificationEvents,
    RNQBPushSubscriptionsModule: QBPushSubscriptions,
    RNQBSettingsModule: QBSettings,
    RNQBUsersModule: QBUsers,
    RNQBWebRTCModule: QBWebRTC
  }
}

export default QB
