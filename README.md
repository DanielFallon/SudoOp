SudoOp
======
###description:
SudoOp is a bukkit plugin that allows normal users to excecute commands as operators 
by being approved to excecute the command by an overseer.

###commands:
* **/sudoOp** [command] [args]
  + creates a new request for command excecution
  + permission: SudoOp.Request
* **/sudoApprove** [Username] [Approval Code]
  + approve an open request for command excectuion
  + permission: SudoOp.Approve
* **/sudoCancel** [Approval Code]
  + cancels one of your open requests
  + permission: SudoOp.Cancel
* **/sudoDeny** [Username] [Approval Code]
  + Denies an open request for command excectuion
  + permission: SudoOp.Deny


###//TODO
* implement database technology for logging
  + allow notifications to be sent to moderators upon login about changes that occurred while they were gone
  + allow moderators to search requests
* create configuration for blacklisted commands
* create configuration for permissions with which approved users will excecute the approved commands
* allow overseers to list open commands
* allow users to list their open commands
