<?php if($error = $sf_user->getFlash('error')): ?>
	<p><?php echo $error ?></p>
<?php endif; ?>

<form action="/user/login" method="POST">
	<p style="display:inline">Username:</p>
	<input name="email" type="text" value="<?php echo $email ?>"> <br />
	<?php if($flash = $sf_user->getFlash('email')): ?>
		<p><?php echo $flash ?></p>
	<?php endif; ?>

	<p style="display:inline">Password:</p>
	<input style="display:inline" name="password" type="password" value="<?php echo $password ?>"> <br />
	<?php if($flash = $sf_user->getFlash('password')): ?>
		<p><?php echo $flash ?></p>
	<?php endif; ?>
	<input type="submit">
</form>