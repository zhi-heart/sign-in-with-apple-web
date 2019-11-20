<html>
<head>
    <title>Sign in with Apple</title>
    <style>
        body {
            text-align: center;
        }

        h3 {
            margin: 100px 0;
        }

        .login-btn {
            display: block;
            margin: 0 auto;
            width: 160px;
            width: 225px;
            height: 45px;
            cursor: pointer;
        }
    </style>
</head>
<body>
<h3>Sign in with Apple</h3>
<script type="text/javascript"
        src="https://appleid.cdn-apple.com/appleauth/static/jsapi/appleid/1/en_US/appleid.auth.js"></script>
<div class="login-btn" id="appleid-signin" data-color="black" data-border="true" data-type="sign in"></div>
<script type="text/javascript">
    AppleID.auth.init({
        clientId: 'com.yanerwu.sign.service',
        scope: 'name email',
        redirectURI: 'https://m.yanerwu.com/apple/callback',
        state: 'state'
    });
</script>
</body>
</html>