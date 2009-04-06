<?php

/**
 * admin actions.
 *
 * @package    epgp
 * @subpackage admin
 * @author     Your name here
 * @version    SVN: $Id: actions.class.php 12479 2008-10-31 10:54:40Z fabien $
 */
class adminActions extends sfActions
{
 /**
  * Executes index action
  *
  * @param sfRequest $request A request object
  */
  public function executeIndex(sfWebRequest $request)
  {
    $this->forward('admin', 'home');
  }
  
  public function executeHome(sfWebRequest $request){
    // todo
  }
  
  public function executeAttendees(sfWebRequest $request){
    $this->forward('adminAttendees', 'index');
  }
  
  public function executeImport(sfWebRequest $request){
    // todo
  }
  
  public function executeRoster(sfWebRequest $request){
    $this->forward('adminRoster', 'index');
  }
  
  public function executeRaids(sfWebRequest $request){
    $this->forward('adminRaids', 'index');
  }
  
  public function executeItems(sfWebRequest $request){
    $this->forward('adminItems', 'index');
  }
  
  public function executeBosses(sfWebRequest $request){
    // todo
  }
  
  public function executeZones(sfWebRequest $request){
    // todo
  }
  
  public function executeDecays(sfWebRequest $request){
    $this->forward('adminDecays', 'index');
  }
}
