/* Copyright (c) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.google.gdata.data.appsforyourdomain;

/**
 * Many of the exceptions which are thrown in the Google Apps for Your Domain
 * GData API have an errorCode equal to one of the codes listed below.
 *
 * These errorCodes will help the AppsForYourDomainService (client library)
 * handle different types of errors which may occur.
 *
 * 
 */

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum AppsForYourDomainErrorCode {
  UnknownError(1000),
  ServerBusy(1001),

  UserDeletedRecently(1100),
  UserSuspended(1101),

  DomainUserLimitExceeded(1200),
  DomainAliasLimitExceeded(1201),
  DomainSuspended(1202),
  DomainFeatureUnavailable(1203),

  EntityExists(1300),
  EntityDoesNotExist(1301),
  EntityNameIsReserved(1302),
  EntityNameNotValid(1303),
  EntityCannotBeModified(1304),
  EntityCannotContainCycle(1305),
  EntityHasMembersCannotDelete(1306),
  EntityQuotaLimitReached(1307),

  InvalidGivenName(1400),
  InvalidFamilyName(1401),
  InvalidPassword(1402),
  InvalidUsername(1403),
  InvalidHashFunctionName(1404),
  InvalidHashDigestLength(1405),
  InvalidEmailAddress(1406),
  InvalidQueryParameterValue(1407),
  InvalidSsoSigningKey(1408),
  InvalidEncryptionPublicKey(1409),
  FeatureUnavailableForUser(1410),
  EncryptionPublicKeyInvalidFormat(1411),
  EncryptionPublicKeyInvalidType(1412),
  EncryptionPublicKeyHasManyUserIds(1413),

  TooManyRecipientsOnEmailList(1500),
  TooManyNicknamesForUser(1501),

  DuplicateDestinations(1601),
  TooManyDestinations(1602),
  InvalidRouteAddress(1603),

  GroupCannotContainCycle(1700),
  InvalidGroupPermissions(1701),

  InvalidDomainEdition(1800),
  InvalidValue(1801),

  DeletePartialFailure(1802);

  private int errorCode;

  private static Map<Integer, AppsForYourDomainErrorCode> errorMap
      = makeErrorMap();

  private static Map<Integer, AppsForYourDomainErrorCode> makeErrorMap() {
    Map<Integer, AppsForYourDomainErrorCode> m
        = new HashMap<Integer, AppsForYourDomainErrorCode>();
    for (AppsForYourDomainErrorCode c : AppsForYourDomainErrorCode.values()) {
      m.put(c.errorCode, c);
    }

    return Collections.unmodifiableMap(m);
  }

  AppsForYourDomainErrorCode(int errorCode) {
    this.errorCode = errorCode;
  }

  @Override
  public String toString() {
    return name() + "(" + errorCode + ")";
  }

  public int getErrorCodeAsInt() {
    return errorCode;
  }

  public static AppsForYourDomainErrorCode getEnumFromInt(Integer errorCode) {
    return errorMap.get(errorCode);
  }
}
