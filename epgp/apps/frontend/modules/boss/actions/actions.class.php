<?php

/**
 * boss actions.
 *
 * @package    epgp
 * @subpackage boss
 * @author     Your name here
 * @version    SVN: $Id: actions.class.php 12479 2008-10-31 10:54:40Z fabien $
 */
class bossActions extends sfActions
{
 /**
  * Executes index action
  *
  * @param sfRequest $request A request object
  */
  public function executeIndex(sfWebRequest $request)
  {
    // todo: list of all bosses
  }
  
  public function executeShow(sfWebRequest $request){
    $bossname = $request->getParameter('name');
    $this->forward404Unless($bossname);
    
    $c0 = new Criteria();
    $c0->add(BossesPeer::NAME, $bossname);
    
    $this->bossinfo = BossesPeer::doSelectOne($c0);
    
    $c = new Criteria();
    $c->add(RaidsPeer::BOSS, $this->bossinfo->getId());
    
    $this->killcount = RaidsPeer::doCount($c);
    
    $this->raids = RaidsPeer::doSelectJoinAll($c);
    
    $this->items = ItemsPeer::doSelectJoinAll($c);
  }
  
}
