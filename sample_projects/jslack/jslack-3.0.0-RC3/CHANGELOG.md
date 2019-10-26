# jSlack Release Notes

## version 3.0.0 (2019-10-XX)

* 8851614 Add user.is_workflow_bot in response by Kazuhiro Sera
* 9a71bc8 Add historical data support and incoming webhooks in InstallationService #234 by Kazuhiro Sera
* 382c311 Fix #235 MultiTeamsAuthorization fails due to missing team_id / enterprise_id in ActionContext by Kazuhiro Sera
* 395a09a Mark workspace app related properties as deprecated by Kazuhiro Sera
* 638d43a Fix #230 selectedUsers in ViewState is missing by Kazuhiro Sera
* 7c783b3 Add bot_profile etc (added recently) by Kazuhiro Sera
* eaf6ced Fix dnd.teamInfo has been broken by Kazuhiro Sera
* f6b1044 Add Lightning README by Kazuhiro Sera
* 995c587 Add OAuth flow support in Lightning by Kazuhiro Sera
* 162dab1 Fix oauth.access API to use basic auth by Kazuhiro Sera
* 783507b Add Lightning, new module to build Slack apps quickly ⚡ by Kazuhiro Sera
* e5deef6 Apply a bunch of improvements for Slack app dev by Kazuhiro Sera
* 773a699 Add include_num_members to conversations.info params by Kazuhiro Sera

### Incompatibility

The following classes are no longer internal classes. They are in the same package as independent ones.
 
* DialogSuggestionResponse.Option
* DialogSuggestionResponse.OptionGroup
* BlockSuggestionResponse.Option
* BlockSuggestionResponse.OptionGroup

## version 2.2.3 (2019-10-02)

* c27dbd1 #224 Add block_suggestion support by Kazuhiro Sera
* 4902d12 Change the type in view payloads to be immutable by Kazuhiro Sera

## version 2.2.2 (2019-10-01)

* f6b2cde #223 Add view payload JSON samples by Kazuhiro Sera
* 659abe9 #223 Add multi select blocks & fix input block errors by Kazuhiro Sera

## version 2.2.1 (2019-10-01)

* 08ba6d4 Add view to block_actions payload (ref #217) by Kazuhiro Sera
* fe4f4d6 Update search results to be compatible with the latest test results by Kazuhiro Sera
* f57235e Add a bunch of pref actions newly added in Audit Logs API by Kazuhiro Sera
* 565f4a6 Fix #220 rich_text blocks support by Kazuhiro Sera

## version 2.2.0 (2019-09-28)

* ebcd160 #217 Add Block Kit in Modals support by Kazuhiro Sera
* e867f4e Fix #218 by adding missing properties in event payloads by Kazuhiro Sera
* a7448d9 Fix #128 chat.update test issues by Kazuhiro Sera

## version 2.1.4 (2019-09-12)

* 7fce724 Fix #214 by providing EventTypeExtractor interface by Kazuhiro Sera

## version 2.1.3 (2019-09-05)

* ff74841 subtype property for the app_mention event (#213) by Subrahmanyam
* 98e6dd0 Add shared-channel related attributes to conversation by Kazuhiro Sera

## version 2.1.2 (2019-08-28)

* 1be4469 Add status API client by Kazuhiro Sera
* 9c26259 Add pref.ent_required_browser_name to known audit actions by Kazuhiro Sera

## version 2.1.1 (2019-08-24)

* 7bc8b9d Change the types of user.enterprise_user.is_admin, is_owner (string -> boolean) by Kazuhiro Sera

`user.enterprise_user.is_admin`, `user.enterprise_user.is_owner` are added in 2.1.0. 
We assume no one uses the version as it was released on the same day. 
But, if you had to deal with the incompatibility in your projects, we're sorry for that inconvenience.

## version 2.1.0 (2019-08-24)

* 5034343 Add admin.apps APIs to the library by Kazuhiro Sera
* 3962f7b Fix #208 conversation.is_moved by Kazuhiro Sera

### Incompatibility

The following two classes have been re-packaged.

* `com.github.seratch.jslack.api.methods.request.admin.AdminUsersSessionResetRequest` -> `com.github.seratch.jslack.api.methods.request.admin.users.AdminUsersSessionResetRequest`
* `com.github.seratch.jslack.api.methods.response.admin.AdminUsersSessionResetResponse` -> `com.github.seratch.jslack.api.methods.response.admin.users.AdminUsersSessionResetResponse`

## version 2.0.0 (2019-08-17)

* 93f8146 Bump okhttp version to 4.1.0 - fixes #179 by Kazuhiro Sera
* 4b98417 Add a constants for Audit Logs Actions by Kazuhiro Sera
* e1cb75d Improve SCIM, Audit API clients by Kazuhiro Sera
* 5d39d92 Remove starchart due to its unstability by Kazuhiro Sera
* 99b41f1 Rename blocksText to blocksAsString + support similar methods too by Kazuhiro Sera
* a430097 modified the ChatPostMessage to set blocks as string by Subbu

## Memo

```
git log --pretty=format:'%h %s by %an' --abbrev-commit | grep -v "Merge pull request " | head -50
```
