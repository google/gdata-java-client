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


package com.google.gdata.data.projecthosting;

import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Link;

import java.util.List;

/**
 * Google Code Project Issues Feed entry.
 *
 * 
 */
public class IssuesEntry extends BaseEntry<IssuesEntry> {

  /**
   * Default mutable constructor.
   */
  public IssuesEntry() {
    super();
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link BaseEntry} instance.
   *
   * @param sourceEntry source entry
   */
  public IssuesEntry(BaseEntry<?> sourceEntry) {
    super(sourceEntry);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(IssuesEntry.class)) {
      return;
    }
    super.declareExtensions(extProfile);
    extProfile.declare(IssuesEntry.class, BlockedOn.getDefaultDescription(false,
        true));
    new BlockedOn().declareExtensions(extProfile);
    extProfile.declare(IssuesEntry.class, Blocking.getDefaultDescription(false,
        true));
    new Blocking().declareExtensions(extProfile);
    extProfile.declare(IssuesEntry.class, Cc.getDefaultDescription(false,
        true));
    new Cc().declareExtensions(extProfile);
    extProfile.declare(IssuesEntry.class, ClosedDate.class);
    extProfile.declare(IssuesEntry.class, Id.class);
    extProfile.declare(IssuesEntry.class, Label.getDefaultDescription(false,
        true));
    extProfile.declare(IssuesEntry.class,
        IssuesLink.getDefaultDescription(false, true));
    extProfile.declare(IssuesEntry.class, MergedInto.class);
    new MergedInto().declareExtensions(extProfile);
    extProfile.declare(IssuesEntry.class, Owner.class);
    new Owner().declareExtensions(extProfile);
    extProfile.declare(IssuesEntry.class, SendEmail.class);
    extProfile.declare(IssuesEntry.class, Stars.class);
    extProfile.declare(IssuesEntry.class, State.class);
    extProfile.declare(IssuesEntry.class, Status.class);
  }

  /**
   * Returns the blocked ons.
   *
   * @return blocked ons
   */
  public List<BlockedOn> getBlockedOns() {
    return getRepeatingExtension(BlockedOn.class);
  }

  /**
   * Adds a new blocked on.
   *
   * @param blockedOn blocked on
   */
  public void addBlockedOn(BlockedOn blockedOn) {
    getBlockedOns().add(blockedOn);
  }

  /**
   * Returns whether it has the blocked ons.
   *
   * @return whether it has the blocked ons
   */
  public boolean hasBlockedOns() {
    return hasRepeatingExtension(BlockedOn.class);
  }

  /**
   * Returns the blockings.
   *
   * @return blockings
   */
  public List<Blocking> getBlockings() {
    return getRepeatingExtension(Blocking.class);
  }

  /**
   * Adds a new blocking.
   *
   * @param blocking blocking
   */
  public void addBlocking(Blocking blocking) {
    getBlockings().add(blocking);
  }

  /**
   * Returns whether it has the blockings.
   *
   * @return whether it has the blockings
   */
  public boolean hasBlockings() {
    return hasRepeatingExtension(Blocking.class);
  }

  /**
   * Returns the list of CCed users.
   *
   * @return list of CCed users
   */
  public List<Cc> getCcs() {
    return getRepeatingExtension(Cc.class);
  }

  /**
   * Adds a new list of CCed user.
   *
   * @param cc list of CCed user
   */
  public void addCc(Cc cc) {
    getCcs().add(cc);
  }

  /**
   * Returns whether it has the list of CCed users.
   *
   * @return whether it has the list of CCed users
   */
  public boolean hasCcs() {
    return hasRepeatingExtension(Cc.class);
  }

  /**
   * Returns the closed date.
   *
   * @return closed date
   */
  public ClosedDate getClosedDate() {
    return getExtension(ClosedDate.class);
  }

  /**
   * Sets the closed date.
   *
   * @param closedDate closed date or <code>null</code> to reset
   */
  public void setClosedDate(ClosedDate closedDate) {
    if (closedDate == null) {
      removeExtension(ClosedDate.class);
    } else {
      setExtension(closedDate);
    }
  }

  /**
   * Returns whether it has the closed date.
   *
   * @return whether it has the closed date
   */
  public boolean hasClosedDate() {
    return hasExtension(ClosedDate.class);
  }

  /**
   * Returns the id.
   *
   * @return id
   */
  public Id getIssueId() {
    return getExtension(Id.class);
  }

  /**
   * Sets the id.
   *
   * @param issueId id or <code>null</code> to reset
   */
  public void setIssueId(Id issueId) {
    if (issueId == null) {
      removeExtension(Id.class);
    } else {
      setExtension(issueId);
    }
  }

  /**
   * Returns whether it has the id.
   *
   * @return whether it has the id
   */
  public boolean hasIssueId() {
    return hasExtension(Id.class);
  }

  /**
   * Returns the labels.
   *
   * @return labels
   */
  public List<Label> getLabels() {
    return getRepeatingExtension(Label.class);
  }

  /**
   * Adds a new label.
   *
   * @param label label
   */
  public void addLabel(Label label) {
    getLabels().add(label);
  }

  /**
   * Returns whether it has the labels.
   *
   * @return whether it has the labels
   */
  public boolean hasLabels() {
    return hasRepeatingExtension(Label.class);
  }

  /**
   * Returns the merged into.
   *
   * @return merged into
   */
  public MergedInto getMergedInto() {
    return getExtension(MergedInto.class);
  }

  /**
   * Sets the merged into.
   *
   * @param mergedInto merged into or <code>null</code> to reset
   */
  public void setMergedInto(MergedInto mergedInto) {
    if (mergedInto == null) {
      removeExtension(MergedInto.class);
    } else {
      setExtension(mergedInto);
    }
  }

  /**
   * Returns whether it has the merged into.
   *
   * @return whether it has the merged into
   */
  public boolean hasMergedInto() {
    return hasExtension(MergedInto.class);
  }

  /**
   * Returns the owner.
   *
   * @return owner
   */
  public Owner getOwner() {
    return getExtension(Owner.class);
  }

  /**
   * Sets the owner.
   *
   * @param owner owner or <code>null</code> to reset
   */
  public void setOwner(Owner owner) {
    if (owner == null) {
      removeExtension(Owner.class);
    } else {
      setExtension(owner);
    }
  }

  /**
   * Returns whether it has the owner.
   *
   * @return whether it has the owner
   */
  public boolean hasOwner() {
    return hasExtension(Owner.class);
  }

  /**
   * Returns the send email.
   *
   * @return send email
   */
  public SendEmail getSendEmail() {
    return getExtension(SendEmail.class);
  }

  /**
   * Sets the send email.
   *
   * @param sendEmail send email or <code>null</code> to reset
   */
  public void setSendEmail(SendEmail sendEmail) {
    if (sendEmail == null) {
      removeExtension(SendEmail.class);
    } else {
      setExtension(sendEmail);
    }
  }

  /**
   * Returns whether it has the send email.
   *
   * @return whether it has the send email
   */
  public boolean hasSendEmail() {
    return hasExtension(SendEmail.class);
  }

  /**
   * Returns the stars.
   *
   * @return stars
   */
  public Stars getStars() {
    return getExtension(Stars.class);
  }

  /**
   * Sets the stars.
   *
   * @param stars stars or <code>null</code> to reset
   */
  public void setStars(Stars stars) {
    if (stars == null) {
      removeExtension(Stars.class);
    } else {
      setExtension(stars);
    }
  }

  /**
   * Returns whether it has the stars.
   *
   * @return whether it has the stars
   */
  public boolean hasStars() {
    return hasExtension(Stars.class);
  }

  /**
   * Returns the state.
   *
   * @return state
   */
  public State getState() {
    return getExtension(State.class);
  }

  /**
   * Sets the state.
   *
   * @param state state or <code>null</code> to reset
   */
  public void setState(State state) {
    if (state == null) {
      removeExtension(State.class);
    } else {
      setExtension(state);
    }
  }

  /**
   * Returns whether it has the state.
   *
   * @return whether it has the state
   */
  public boolean hasState() {
    return hasExtension(State.class);
  }

  /**
   * Returns the status.
   *
   * @return status
   */
  public Status getStatus() {
    return getExtension(Status.class);
  }

  /**
   * Sets the status.
   *
   * @param status status or <code>null</code> to reset
   */
  public void setStatus(Status status) {
    if (status == null) {
      removeExtension(Status.class);
    } else {
      setExtension(status);
    }
  }

  /**
   * Returns whether it has the status.
   *
   * @return whether it has the status
   */
  public boolean hasStatus() {
    return hasExtension(Status.class);
  }

  /**
   * Returns the replies Project Hosting Link class.
   *
   * @return Replies Project Hosting Link class or {@code null} for none.
   */
  public Link getAtomRepliesLink() {
    return getLink(IssuesLink.Rel.REPLIES, Link.Type.ATOM);
  }

  @Override
  protected void validate() {
  }

  @Override
  public String toString() {
    return "{IssuesEntry " + super.toString() + "}";
  }

}

