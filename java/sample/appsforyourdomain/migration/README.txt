AppsForYourDomainMigrationClient: A simple application that
demonstrates how to migrate email messages to a Google Apps
email account.

To migrate mail as a Google Apps user, invoke as follows:

    java AppsForYourDomainMigrationClient --username <user>
      --password <password> --domain <domain>

For example:

    java AppsForYourDomainMigrationClient --username jdoe
      --password mypass --domain example.com

To migrate mail as a Google Apps administrator, also specify
the destination mailbox (username) for the mail upload:

    java AppsForYourDomainMigrationClient --username <user>
      --password <password> --domain <domain>
      --destination_user <username>

For example:

    java AppsForYourDomainMigrationClient --username admin
      --password mypass --domain example.com --destination_user jdoe

By default, the sample application will migrate a simple,
example email message.  You can also specify a message on disk,
assuming it is already in the RFC822 standard format, by passing
the --data_file argument:

    java AppsForYourDomainMigrationClient --username jdoe
      --password mypass --domain example.com
      --data_file /home/jdoe/my_rfc822_message.txt

