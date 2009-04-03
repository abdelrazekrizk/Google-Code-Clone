<?php

/**
 * raid actions.
 *
 * @package    epgp
 * @subpackage raid
 * @author     Your name here
 * @version    SVN: $Id: actions.class.php 12479 2008-10-31 10:54:40Z fabien $
 */
class raidActions extends sfActions
{
  public function executeShow(sfRequest $request){
    $raidId = $request->getParameter('id');
    $this->forward404Unless($raidId);
    
    $c = new Criteria();
    $c->add(RaidsPeer::ID, $raidId);
    list($this->raidinfo) = RaidsPeer::doSelectJoinAll($c);
    
    $this->attendees = AttendeesPeer::doSelectJoinRoster($c);
    
    $this->items = ItemsPeer::doSelectJoinAll($c);
  }
  
  public function getAttendees($raidid){
    $c = new Criteria();
    $c->addJoin(AttendeesPeer::PLAYERID, RosterPeer::ID);
    $c->addJoin(RosterPeer::CHARCLASS, ClassesPeer::ID);
    $c->addSelectColumn(ClassesPeer::NAME);
  }
}
