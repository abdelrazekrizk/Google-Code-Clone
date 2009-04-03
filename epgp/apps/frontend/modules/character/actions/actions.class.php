<?php

/**
 * character actions.
 *
 * @package    epgp
 * @subpackage character
 * @author     Your name here
 * @version    SVN: $Id: actions.class.php 12479 2008-10-31 10:54:40Z fabien $
 */
class characterActions extends sfActions
{
 /**
  * Executes index action
  *
  * @param sfRequest $request A request object
  */
  public function executeIndex(sfWebRequest $request)
  {
    $this->forward('character', 'show');
  }
  
  public function executeShow(sfWebRequest $request){
    $charname = $request->getParameter('name');
    $this->forward404Unless($charname);
    
    $c = new Criteria();
    $c->add(RosterPeer::NAME, $charname);
    list($this->charinfo) = RosterPeer::doSelectJoinClasses($c);
    
    $c = new Criteria();
    $c->add(RosterPeer::NAME, $charname);
    $this->attendanceRecords = AttendeesPeer::doSelectJoinAll($c);
    
    $c = new Criteria();
    $c->add(ItemsPeer::PLAYERID, $this->charinfo->getId());
    $this->items = ItemsPeer::doSelectJoinAll($c);
  }
}
