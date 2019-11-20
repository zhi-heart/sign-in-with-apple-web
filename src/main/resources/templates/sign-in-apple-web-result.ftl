<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Sign in with Apple - DEMO</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<style>
    body {
        text-align: center;
    }

    h3 {
        margin: 100px 0;
    }
</style>
<body>
<div>
    <p>
        ID ${idTokenPayload.sub!}
    </p>
    <p>
        邮箱 ${idTokenPayload.email!}
    </p>
    <p>
        是否用户邮箱 ${idTokenPayload.emailVerified?c!}
    </p>
</div>
</body>
</html>