<?php

/**
 * user actions.
 */
class userActions extends sfActions {
	/**
	 * Executes login action
	 *
	 * @param sfRequest $request A request object
	 */
	public function executeLogin(sfWebRequest $request) {
		// if we were POST'ed data, this is a login attempt
		if($request->getMethod() === sfRequest::POST) {
			$this->doLogin($request);
		}

		if($this->getUser()->isAuthenticated()){
			$this->forwardPostLogin();
		}

		// if we haven't been forwarded yet, just display the login page.
	}

	/**
	 * @param sfWebRequest $request.
	 * @return Account false if validation fails, Account object if succeeds.
	 */
	public function doLogin(sfWebRequest $request)
	{
		// validate that user and password was entered
		$this->email = $request->getPostParameter('email', false);
		$this->password = $request->getPostParameter('password', false);

		$validated = true;
		if($this->email === false || $this->email == '') {
			// fail email validation
			$this->getUser()->setFlash('email', 'You must specify an e-mail');
			$validated = false;
		}

		if($this->password === false || $this->password == '') {
			// fail password validation
			$this->getUser()->setFlash('password', 'You must specify a password');
			$validated = false;
		}

		if(!$validated) return false;

		// validate that the credentials are valid
		$account = Doctrine_Query::create()->from('Account a')
			->where('a.email = ?', $this->email)
			->addWhere('a.password = ?', $this->password)
			->fetchOne();

		$account instanceof Account;

		if(!$account){
			$this->getUser()->setFlash('error', 'Invalid credentials');
			return false;
		}
		else {
			$this->getUser()->setAuthenticated(true);
			$this->getUser()->setAttribute('AccountId', $account->id);

			if($account->isAdmin)
				$this->getUser()->addCredential('admin');

			return $account;
		}
	}

	public function forwardPostLogin()
	{
		if($this->getUser()->hasCredential('admin')){
			$this->forward('admin', 'index');
		} else {
			$this->forward('/profile/read/id/'.$account->id);
		}
	}

	public function preExecute(){
		$this->email = '';
		$this->password = '';
	}

	public function executeAlreadyLoggedIn(){
		// do nothing action
	}

	public function executeLogout(){
		$this->getUser()->setAuthenticated(false);
		$this->getUser()->clearCredentials();
		$this->getUser()->getAttributeHolder()->clear();
	}
}
