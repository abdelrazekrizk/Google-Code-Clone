<?php

//$account = new Account();
//
//$account->accountType = 0;
//$account->email = "test@test.com";
//$account->firstname = "First";
//$account->lastname = "Last";
//$account->password = "Password";
//$account->save();

$account = Doctrine_Query::create()->from('Account a')->fetchOne();
var_dump($account->accountType);
