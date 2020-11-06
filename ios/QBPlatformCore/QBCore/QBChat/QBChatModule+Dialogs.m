//
//  QBChatModule+Dialogs.m
//  crossplatform-sdk
//
//  Created by Injoit on 26.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBChatModule+Dialogs.h"
#import "QBResponsePage+QBSerializer.h"
#import "QBChatDialog+QBSerializer.h"

@implementation QBChatModule (Dialogs)

- (void)getDialogs:(NSDictionary *)info
          resolver:(QBResolveBlock)resolve
          rejecter:(QBRejectBlock)reject {
  NSNumber *limit = info[QBPageKey.limit];
  NSNumber *skip = info[QBPageKey.skip];
  QBResponsePage *page =
  [QBResponsePage responsePageWithLimit:limit ? limit.integerValue : 100
                                   skip:skip ? skip.integerValue : 0];
  
  NSMutableDictionary *extendedRequest = @{}.mutableCopy;
  NSDictionary *sortInfo = info[SortKey.sort];
  if (sortInfo) {
    [extendedRequest addEntriesFromDictionary:[sortInfo toSortData]];
  }
  
  NSDictionary *filterInfo = info[FilterKey.filter];
  if (filterInfo) {
    [extendedRequest addEntriesFromDictionary:[filterInfo toFilterData]];
  }
  
  __weak __typeof(self)weakSelf = self;
  [QBRequest dialogsForPage:page
            extendedRequest:extendedRequest.copy
               successBlock:^(QBResponse *response,
                              NSArray *dialogObjects,
                              NSSet *dialogsUsersIDs,
                              QBResponsePage *page) {
    [weakSelf addDialogsToCache:dialogObjects];
    NSError *error = nil;
    NSArray *resultArray = [dialogObjects toQBResultArray:&error];
    if ([error reject:reject]) {
      return;
    }
    NSDictionary *resultPage = [page toQBResultData:&error];
    if ([error reject:reject]) {
      return;
    }
    
    NSMutableDictionary *result = @{ QBChatKey.dialogs: resultArray }.mutableCopy;
    [result addEntriesFromDictionary:resultPage];
    resolve(result.copy);
  } errorBlock:^(QBResponse *response) {
    [response reject:reject];
  }];
}


- (void)getDialogsCount:(NSDictionary *)info
               resolver:(QBResolveBlock)resolve
               rejecter:(QBRejectBlock)reject {
  NSMutableDictionary *extendedRequest = @{}.mutableCopy;
  NSDictionary *filterInfo = info[FilterKey.filter];
  if (filterInfo) {
    [extendedRequest addEntriesFromDictionary:[filterInfo toFilterData]];
  }
  
  [QBRequest countOfDialogsWithExtendedRequest:extendedRequest.copy
                                  successBlock:^(QBResponse *response,
                                                 NSUInteger count) {
    if (resolve) {
      resolve(@(count));
    }
  } errorBlock:^(QBResponse *response) {
    [response reject:reject];
  }];
}

- (void)createDialog:(NSDictionary *)info
            resolver:(QBResolveBlock)resolve
            rejecter:(QBRejectBlock)reject {
  NSMutableArray<NSNumber *>*occupantsIds = nil;
  if ([info[QBDialogKey.occupantsIds] isKindOfClass:[NSArray class]]) {
    NSArray<NSNumber *>*Ids = info[QBDialogKey.occupantsIds];
    occupantsIds = Ids.mutableCopy;
  }
  NSNumber *typeNumber = info[QBDialogKey.type];
  QBChatDialogType type = QBChatDialogTypeGroup;
  if (occupantsIds.count && !typeNumber) {
    if (![occupantsIds containsObject:@(self.currentId)]) {
      [occupantsIds addObject:@(self.currentId)];
    }
    // when the current id or few recipients ids in the occupantsIds
    if (occupantsIds.count > 2 || occupantsIds.count == 1) {
      type = QBChatDialogTypeGroup;
    } else if (occupantsIds.count == 2) {
      type = QBChatDialogTypePrivate;
    } else {
      type = QBChatDialogTypePublicGroup;
    }
  } else if (typeNumber == nil) {
    type = QBChatDialogTypePublicGroup;
  } else {
    QBChatDialogType type = typeNumber.integerValue;
    if (type < 1 || type > 3) {
      type = QBChatDialogTypeGroup;
    }
    if (type != QBChatDialogTypePublicGroup) {
      if (![occupantsIds containsObject:@(self.currentId)]) {
        [occupantsIds addObject:@(self.currentId)];
      }
    }
  }
  
  NSString *name = info[QBDialogKey.name];
  
  QBChatDialog *chatDialog =
  [[QBChatDialog alloc] initWithDialogID:nil
                                    type:type];
  
  chatDialog.name = name;
  chatDialog.occupantIDs = type == QBChatDialogTypePublicGroup ? nil : occupantsIds.copy;
  
  __weak __typeof(self)weakSelf = self;
  [QBRequest createDialog:chatDialog
             successBlock:^(QBResponse *response,
                            QBChatDialog *createdDialog) {
    [weakSelf addDialogsToCache:@[createdDialog]];
    if (createdDialog.type != QBChatDialogTypePrivate && createdDialog.isJoined == NO) {
      [createdDialog joinWithCompletionBlock:^(NSError * _Nullable error) {
        if (![error reject:reject] && resolve) {
          [createdDialog toQBResultDataWithResolver:resolve
                                           rejecter:reject];
        }
      }];
    } else {
      [createdDialog toQBResultDataWithResolver:resolve rejecter:reject];
    }
  } errorBlock:^(QBResponse *response) {
    [response reject:reject];
  }];
}

- (void)updateDialog:(NSDictionary *)info
            resolver:(QBResolveBlock)resolve
            rejecter:(QBRejectBlock)reject {
  __weak __typeof(self)weakSelf = self;
  [self dialogWithInfo:info
               success:^(QBChatDialog *dialog) {
    NSArray<NSNumber *>*addUsers = info[QBChatKey.addUsers];
    NSArray<NSNumber *>*removeUsers = info[QBChatKey.removeUsers];
    NSString *name = info[QBDialogKey.name];
    if ((removeUsers.count &&
         weakSelf.currentId != dialog.userID) &&
        (removeUsers.count > 1 ||
         ![removeUsers containsObject:@(weakSelf.currentId)])) {
      [NSError reject:reject
              message:@"Only dialog's creator(owner) can remove any users from occupants_ids."];
      return;
    }
    QBChatDialog *updateDialog =
    [[QBChatDialog alloc] initWithDialogID:dialog.ID
                                      type:QBChatDialogTypeGroup];
    NSMutableArray<NSString *>*pushOccupantsIDs = @[].mutableCopy;
    for (NSNumber *numberId in addUsers) {
      [pushOccupantsIDs addObject:numberId.stringValue];
    }
    if (pushOccupantsIDs.count) {
      updateDialog.pushOccupantsIDs = pushOccupantsIDs.copy;
    }
    
    __block NSMutableArray<NSString *>*pullOccupantsIDs = @[].mutableCopy;
    __block BOOL needRemoveSeparate = NO;
    for (NSNumber *numberId in removeUsers) {
      [pullOccupantsIDs addObject:numberId.stringValue];
    }
    if (pullOccupantsIDs.count) {
      if (pushOccupantsIDs.count) {
        needRemoveSeparate = YES;
      } else {
        updateDialog.pullOccupantsIDs = pullOccupantsIDs.copy;
      }
    }
    
    if (name != nil) {
      updateDialog.name = name;
    }
    
    [QBRequest updateDialog:updateDialog
               successBlock:^(QBResponse *responce,
                              QBChatDialog *dialog) {
      if (needRemoveSeparate) {
        QBChatDialog *removeUsersDialog =
        [[QBChatDialog alloc] initWithDialogID:dialog.ID
                                          type:QBChatDialogTypeGroup];
        removeUsersDialog.pullOccupantsIDs = pullOccupantsIDs.copy;
        [QBRequest updateDialog:removeUsersDialog
                   successBlock:^(QBResponse *responce,
                                  QBChatDialog *dialog) {
          [weakSelf addDialogsToCache:@[dialog]];
          [dialog toQBResultDataWithResolver:resolve
                                    rejecter:reject];
        } errorBlock:^(QBResponse *response) {
          [response reject:reject];
        }];
        return;
      }
      [weakSelf addDialogsToCache:@[dialog]];
      [dialog toQBResultDataWithResolver:resolve rejecter:reject];
    } errorBlock:^(QBResponse *response) {
      [response reject:reject];
    }];
  } rejecter:reject];
}

- (void)deleteDialog:(NSDictionary *)info
            resolver:(QBResolveBlock)resolve
            rejecter:(QBRejectBlock)reject {
  NSObject *dialogIdObject = info[QBChatKey.dialogId];
  if ([NSError reject:reject
         checkerClass:NSString.class
               object:dialogIdObject
            objectKey:QBChatKey.dialogId]) {
    return;
  }
  NSString *dialogId = (NSString *)dialogIdObject;
  [self deleteDialog:dialogId force:YES resolver:resolve rejecter:reject];
}

- (void)joinDialog:(NSDictionary *)info
          resolver:(QBResolveBlock)resolve
          rejecter:(QBRejectBlock)reject {
  [self dialogWithInfo:info
               success:^(QBChatDialog *dialog) {
    if (dialog.type == QBChatDialogTypePrivate) {
      [dialog toQBResultDataWithResolver:resolve
                                rejecter:reject];
      return;
    }
    [dialog joinWithCompletionBlock:^(NSError * _Nullable error) {
      if (![error reject:reject] && resolve) {
        [dialog toQBResultDataWithResolver:resolve
                                  rejecter:reject];
      }
    }];
  } rejecter:reject];
}

- (void)getOnlineUsers:(NSDictionary *)info
              resolver:(QBResolveBlock)resolve
              rejecter:(QBRejectBlock)reject {
  [self dialogWithInfo:info
               success:^(QBChatDialog *dialog) {
    [dialog requestOnlineUsersWithCompletionBlock:
     ^(NSMutableArray<NSNumber *> * _Nullable onlineUsers,
       NSError * _Nullable error) {
      if (![error reject:reject] && resolve) {
        resolve(onlineUsers.copy);
      }
    }];
  } rejecter:reject];
}

- (void)leaveDialog:(NSDictionary *)info
           resolver:(QBResolveBlock)resolve
           rejecter:(QBRejectBlock)reject {
  __weak __typeof(self)weakSelf = self;
  [self dialogWithInfo:info
               success:^(QBChatDialog *dialog) {
    if (dialog.type == QBChatDialogTypePrivate ||
        dialog.isJoined == NO) {
      if (dialog.type == QBChatDialogTypePublicGroup) {
        resolve(nil);
        return;
      }
      [weakSelf deleteDialog:dialog.ID
                       force:NO
                    resolver:resolve
                    rejecter:reject];
      return;
    }
    
    [dialog leaveWithCompletionBlock:^(NSError * _Nullable error) {
      if (![error reject:reject] && resolve) {
        [weakSelf deleteDialog:dialog.ID
                         force:NO
                      resolver:resolve
                      rejecter:reject];
      }
    }];
  } rejecter:reject];
}

//MARK - Internal methods

- (void)deleteDialog:(NSString *)dialogId
               force:(BOOL)force
            resolver:(QBResolveBlock)resolve
            rejecter:(QBRejectBlock)reject {
  NSSet *dialogsIDs = [NSSet setWithObject:dialogId];
  __weak __typeof(self)weakSelf = self;
  [QBRequest deleteDialogsWithIDs:dialogsIDs
                      forAllUsers:force
                     successBlock:^(QBResponse *response,
                                    NSArray *deletedObjectsIDs,
                                    NSArray *notFoundObjectsIDs,
                                    NSArray *wrongPermissionsObjectsIDs) {
    if (resolve) {
      NSMutableArray *dialogsToRemove = @[].mutableCopy;
      for (NSString *dialogId in deletedObjectsIDs) {
        for (QBChatDialog *dialog in weakSelf.dialogsCache) {
          if ([dialog.ID isEqualToString:dialogId]) {
            [dialogsToRemove addObject:dialog];
            continue;
          }
        }
      }
      [weakSelf removeDialogsFromCache:dialogsToRemove];
      resolve(nil);
    }
  } errorBlock:^(QBResponse *response) {
    [response reject:reject];
  }];
}

@end
