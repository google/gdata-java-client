function BlogRenderer(container) {
  this.container_ = container;
}

BlogRenderer.prototype.render = function(blog) {
  if (!blog || !blog.items) return;
  for (var i = 0; i < blog.items.length; i++) {
    var item = blog.items[i];
    var entry = this.createDiv(this.container_, "entry");
    this.createLink(entry, "title", item.alternate.href, item.title);
    this.createDiv(entry, "author", "Posted by " + item.author);
    this.createDiv(entry, "body", item.contentSnippet);
  }
}

BlogRenderer.prototype.createDiv = function(parent, className, opt_text) {
  var div = document.createElement("div");
  div.className = className;
  parent.appendChild(div);
  if (opt_text) {
    div.appendChild(document.createTextNode(opt_text));
  }
  return div;
}

BlogRenderer.prototype.createLink = function(parent, className, href, text) {
  var div = document.createElement("div");
  div.className = className;
  parent.appendChild(div);
  var link = document.createElement("a");
  link.href = href;
  div.appendChild(link);
  link.appendChild(document.createTextNode(text));
  return div;
}
