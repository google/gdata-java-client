Imports System.Xml
Imports Google.GData.Client
Imports Google.GData.GoogleBase




Public Class MainForm
    Inherits System.Windows.Forms.Form
    Dim batchFeed As GBaseFeed
    Dim entryArray As New ArrayList


#Region " Windows Form Designer generated code "

    Public Sub New()
        MyBase.New()

        'This call is required by the Windows Form Designer.
        InitializeComponent()

        'Add any initialization after the InitializeComponent() call

    End Sub

    'Form overrides dispose to clean up the component list.
    Protected Overloads Overrides Sub Dispose(ByVal disposing As Boolean)
        If disposing Then
            If Not (components Is Nothing) Then
                components.Dispose()
            End If
        End If
        MyBase.Dispose(disposing)
    End Sub

    'Required by the Windows Form Designer
    Private components As System.ComponentModel.IContainer

    'NOTE: The following procedure is required by the Windows Form Designer
    'It can be modified using the Windows Form Designer.  
    'Do not modify it using the code editor.
    Friend WithEvents Label1 As System.Windows.Forms.Label
    Friend WithEvents Label2 As System.Windows.Forms.Label
    Friend WithEvents Label3 As System.Windows.Forms.Label
    Friend WithEvents Label4 As System.Windows.Forms.Label
    Friend WithEvents Username As System.Windows.Forms.TextBox
    Friend WithEvents Password As System.Windows.Forms.TextBox
    Friend WithEvents AppKey As System.Windows.Forms.TextBox
    Friend WithEvents BaseUri As System.Windows.Forms.TextBox
    Friend WithEvents GetData As System.Windows.Forms.Button
    Friend WithEvents PostData As System.Windows.Forms.Button
    Friend WithEvents BaseGrid As System.Windows.Forms.DataGrid
    Friend WithEvents ItemCount As System.Windows.Forms.Label
    <System.Diagnostics.DebuggerStepThrough()> Private Sub InitializeComponent()
        Me.Label1 = New System.Windows.Forms.Label
        Me.Username = New System.Windows.Forms.TextBox
        Me.Password = New System.Windows.Forms.TextBox
        Me.Label2 = New System.Windows.Forms.Label
        Me.AppKey = New System.Windows.Forms.TextBox
        Me.Label3 = New System.Windows.Forms.Label
        Me.BaseUri = New System.Windows.Forms.TextBox
        Me.Label4 = New System.Windows.Forms.Label
        Me.GetData = New System.Windows.Forms.Button
        Me.PostData = New System.Windows.Forms.Button
        Me.BaseGrid = New System.Windows.Forms.DataGrid
        Me.ItemCount = New System.Windows.Forms.Label
        CType(Me.BaseGrid, System.ComponentModel.ISupportInitialize).BeginInit()
        Me.SuspendLayout()
        '
        'Label1
        '
        Me.Label1.Location = New System.Drawing.Point(16, 24)
        Me.Label1.Name = "Label1"
        Me.Label1.Size = New System.Drawing.Size(120, 16)
        Me.Label1.TabIndex = 0
        Me.Label1.Text = "Username:"
        '
        'Username
        '
        Me.Username.Location = New System.Drawing.Point(168, 20)
        Me.Username.Name = "Username"
        Me.Username.Size = New System.Drawing.Size(264, 20)
        Me.Username.TabIndex = 1
        Me.Username.Text = "<userName>"
        '
        'Password
        '
        Me.Password.Location = New System.Drawing.Point(168, 48)
        Me.Password.Name = "Password"
        Me.Password.PasswordChar = Microsoft.VisualBasic.ChrW(42)
        Me.Password.Size = New System.Drawing.Size(264, 20)
        Me.Password.TabIndex = 2
        Me.Password.Text = ""
        '
        'Label2
        '
        Me.Label2.Location = New System.Drawing.Point(16, 52)
        Me.Label2.Name = "Label2"
        Me.Label2.Size = New System.Drawing.Size(120, 16)
        Me.Label2.TabIndex = 2
        Me.Label2.Text = "Password:"
        '
        'AppKey
        '
        Me.AppKey.Location = New System.Drawing.Point(168, 72)
        Me.AppKey.Name = "AppKey"
        Me.AppKey.Size = New System.Drawing.Size(264, 20)
        Me.AppKey.TabIndex = 3
        Me.AppKey.Text = ""
        '
        'Label3
        '
        Me.Label3.Location = New System.Drawing.Point(16, 76)
        Me.Label3.Name = "Label3"
        Me.Label3.Size = New System.Drawing.Size(120, 16)
        Me.Label3.TabIndex = 4
        Me.Label3.Text = "Applicationkey:"
        '
        'BaseUri
        '
        Me.BaseUri.Location = New System.Drawing.Point(168, 100)
        Me.BaseUri.Name = "BaseUri"
        Me.BaseUri.Size = New System.Drawing.Size(264, 20)
        Me.BaseUri.TabIndex = 4
        Me.BaseUri.Text = "http://base.google.com/base/feeds/items"
        '
        'Label4
        '
        Me.Label4.Location = New System.Drawing.Point(16, 104)
        Me.Label4.Name = "Label4"
        Me.Label4.Size = New System.Drawing.Size(120, 16)
        Me.Label4.TabIndex = 6
        Me.Label4.Text = "Googlebase URI:"
        '
        'GetData
        '
        Me.GetData.Enabled = False
        Me.GetData.Location = New System.Drawing.Point(544, 16)
        Me.GetData.Name = "GetData"
        Me.GetData.Size = New System.Drawing.Size(136, 32)
        Me.GetData.TabIndex = 5
        Me.GetData.Text = "Get the data..."
        '
        'PostData
        '
        Me.PostData.Enabled = False
        Me.PostData.Location = New System.Drawing.Point(544, 64)
        Me.PostData.Name = "PostData"
        Me.PostData.Size = New System.Drawing.Size(136, 32)
        Me.PostData.TabIndex = 6
        Me.PostData.Text = "Post the data"
        '
        'BaseGrid
        '
        Me.BaseGrid.CaptionVisible = False
        Me.BaseGrid.DataMember = ""
        Me.BaseGrid.HeaderForeColor = System.Drawing.SystemColors.ControlText
        Me.BaseGrid.Location = New System.Drawing.Point(24, 152)
        Me.BaseGrid.Name = "BaseGrid"
        Me.BaseGrid.Size = New System.Drawing.Size(696, 272)
        Me.BaseGrid.TabIndex = 7
        '
        'ItemCount
        '
        Me.ItemCount.Location = New System.Drawing.Point(32, 432)
        Me.ItemCount.Name = "ItemCount"
        Me.ItemCount.Size = New System.Drawing.Size(152, 16)
        Me.ItemCount.TabIndex = 11
        Me.ItemCount.Text = "no items..."
        '
        'MainForm
        '
        Me.AutoScaleBaseSize = New System.Drawing.Size(5, 13)
        Me.ClientSize = New System.Drawing.Size(744, 454)
        Me.Controls.Add(Me.ItemCount)
        Me.Controls.Add(Me.BaseGrid)
        Me.Controls.Add(Me.PostData)
        Me.Controls.Add(Me.GetData)
        Me.Controls.Add(Me.BaseUri)
        Me.Controls.Add(Me.Label4)
        Me.Controls.Add(Me.AppKey)
        Me.Controls.Add(Me.Label3)
        Me.Controls.Add(Me.Password)
        Me.Controls.Add(Me.Label2)
        Me.Controls.Add(Me.Username)
        Me.Controls.Add(Me.Label1)
        Me.Name = "MainForm"
        Me.Text = "VB GoogleBase Demo"
        CType(Me.BaseGrid, System.ComponentModel.ISupportInitialize).EndInit()
        Me.ResumeLayout(False)

    End Sub

#End Region


    Private Sub GetData_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles GetData.Click

        Dim service As GBaseService = New GBaseService(Me.Text, AppKey.Text)
        Dim query As GBaseQuery = New GBaseQuery(Me.BaseUri.Text)

        If (Username.Text.Length > 0) Then
            service.setUserCredentials(Me.Username.Text, Me.Password.Text)
        End If

        Me.Cursor = Cursors.WaitCursor

        Dim feed As GBaseFeed = service.Query(query)

        Dim table As DataTable = CreateDataTable()

        Dim source As New DataSet("source")
        ' Adds two DataTable objects, Suppliers and Products.


        Me.batchFeed = feed

        source.Tables.Add(table)

        Dim i As Integer
        Dim goOn As Boolean
        goOn = True
        i = 0

        While goOn
            ' fill the table
            For Each entry As GBaseEntry In feed.Entries
                Dim row As DataRow

                row = table.NewRow()

                row("id") = entry.Id.Uri.ToString()
                row("Title") = entry.Title.Text
                row("Content") = entry.Content.Content
                row("Author") = entry.Authors.Item(0).Name
                row("ItemType") = entry.GBaseAttributes.ItemType
                table.Rows.Add(row)
                Me.entryArray.Add(entry)
                i = i + 1
            Next

            Me.ItemCount.Text = "Currently: " + i.ToString() + " items"
            Application.DoEvents()

            If goOn = True Or Nothing Is feed.NextChunk() Then
                goOn = False
            Else
                query.Uri = New Uri(feed.NextChunk())
                feed = service.Query(query)
            End If

        End While



        Me.ItemCount.Text = "Total: " + i.ToString() + " items"

        table.AcceptChanges()


        Me.BaseGrid.DataSource = table
        Me.BaseGrid.Text = i.ToString()


        Me.Cursor = Cursors.Default
        Me.PostData.Enabled = True
    End Sub


    Private Function CreateDataTable()
        ' Create a new DataTable.
        Dim myDataTable As DataTable = New DataTable("GBaseTable")
        ' Declare variables for DataColumn and DataRow objects.
        Dim myDataColumn As DataColumn
        ' Create new DataColumn, set DataType, ColumnName and add to DataTable.    
        myDataColumn = New DataColumn
        myDataColumn.DataType = System.Type.GetType("System.String")
        myDataColumn.ColumnName = "Id"
        myDataColumn.ReadOnly = True
        myDataColumn.Unique = True

        ' Add the Column to the DataColumnCollection.
        myDataTable.Columns.Add(myDataColumn)

        ' Create title column.
        myDataColumn = New DataColumn
        myDataColumn.DataType = System.Type.GetType("System.String")
        myDataColumn.ColumnName = "Title"
        myDataColumn.AutoIncrement = False
        myDataColumn.ReadOnly = False
        myDataColumn.Unique = False
        ' Add the column to the table.
        myDataTable.Columns.Add(myDataColumn)

        myDataColumn = New DataColumn
        myDataColumn.DataType = System.Type.GetType("System.String")
        myDataColumn.ColumnName = "Author"
        myDataColumn.AutoIncrement = False
        myDataColumn.ReadOnly = False
        myDataColumn.Unique = False
        ' Add the column to the table.
        myDataTable.Columns.Add(myDataColumn)

        myDataColumn = New DataColumn
        myDataColumn.DataType = System.Type.GetType("System.String")
        myDataColumn.ColumnName = "Content"
        myDataColumn.AutoIncrement = False
        myDataColumn.ReadOnly = False
        myDataColumn.Unique = False
        ' Add the column to the table.
        myDataTable.Columns.Add(myDataColumn)

        myDataColumn = New DataColumn
        myDataColumn.DataType = System.Type.GetType("System.String")
        myDataColumn.ColumnName = "ItemType"
        myDataColumn.AutoIncrement = False
        myDataColumn.ReadOnly = False
        myDataColumn.Unique = False
        ' Add the column to the table.
        myDataTable.Columns.Add(myDataColumn)

        Return myDataTable
    End Function





    Private Sub PostData_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles PostData.Click
        ' we need to walk over the datatable and get the changed/new lines
        Dim table As DataTable = Me.BaseGrid.DataSource

        Dim i As Integer
        i = 0

        Dim service As GBaseService = New GBaseService(Me.Text, AppKey.Text)
        If (Username.Text.Length > 0) Then
            service.setUserCredentials(Me.Username.Text, Me.Password.Text)
        End If

        Dim batchFeed As AtomFeed = New AtomFeed(Me.batchFeed)

        batchFeed.BatchData = New GDataBatchFeedData
        batchFeed.BatchData.Type = GDataBatchOperationType.update

        Dim counter As Integer

        For Each row As DataRow In table.Rows
            If row.RowState <> DataRowState.Unchanged Then
                i = i + 1
                Me.ItemCount.Text = "Changed: " + i.ToString() + " items"
                Application.DoEvents()

                Dim entry As GBaseEntry
                Dim author As AtomPerson

                If row.RowState = DataRowState.Added Then
                    entry = New GBaseEntry
                    author = New AtomPerson
                    entry.Authors.Add(author)
                Else
                    entry = Me.entryArray.Item(counter)
                    author = entry.Authors.Item(0)
                End If
                entry.BatchData = New GDataBatchEntryData
                entry.BatchData.Id = i.ToString()
                If row.RowState <> DataRowState.Deleted Then
                    author.Name = row("Author")
                    entry.Title.Text = row("Title")
                    entry.Content.Content = row("Content")
                    entry.GBaseAttributes.ItemType = row("ItemType")
                End If
                If row.RowState = DataRowState.Added Then
                    entry.BatchData.Type = GDataBatchOperationType.insert
                ElseIf row.RowState = DataRowState.Deleted Then
                    entry.BatchData.Type = GDataBatchOperationType.delete
                ElseIf row.RowState = DataRowState.Modified Then
                    entry.BatchData.Type = GDataBatchOperationType.update
                End If
                batchFeed.Entries.Add(entry)
            End If
            counter = counter + 1
        Next

        If i > 0 Then
            ' upload
            Dim returnFeed As GBaseFeed = service.Batch(batchFeed, New Uri(Me.batchFeed.Batch))


            For Each entry As GBaseEntry In returnFeed.Entries
                If entry.BatchData.Status.Code > 300 Then
                    Me.ItemCount.Text = "errors encountered"
                    Application.DoEvents()
                    Exit Sub
                End If
            Next

            Call Me.GetData_Click(Nothing, Nothing)
        End If


    End Sub

    Private Sub Username_TextChanged(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles Username.TextChanged
        VerifyButtons()
    End Sub

    Private Sub Password_TextChanged(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles Password.TextChanged
        VerifyButtons()
    End Sub

    Private Sub AppKey_TextChanged(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles AppKey.TextChanged
        VerifyButtons()
    End Sub

    Private Sub BaseUri_TextChanged(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles BaseUri.TextChanged
        VerifyButtons()
    End Sub

    Private Sub VerifyButtons()
        If Me.BaseUri.Text.Length > 0 And Me.Username.Text.Length > 0 And Me.Password.Text.Length > 0 And Me.AppKey.Text.Length > 0 Then
            Me.GetData.Enabled = True
            If Not (Me.batchFeed Is Nothing) Then
                Me.PostData.Enabled = True
            End If
        Else
            Me.GetData.Enabled = False
            Me.PostData.Enabled = False
        End If
    End Sub
End Class
