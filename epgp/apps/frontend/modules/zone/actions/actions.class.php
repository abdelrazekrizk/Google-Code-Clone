<?php

/**
 * zone actions.
 *
 * @package    epgp
 * @subpackage zone
 * @author     Your name here
 * @version    SVN: $Id: actions.class.php 12479 2008-10-31 10:54:40Z fabien $
 */
class zoneActions extends sfActions
{
 /**
  * Executes index action
  *
  * @param sfRequest $request A request object
  */
  public function executeIndex(sfWebRequest $request)
  {
    // todo: show all zones
  }
  
  public function executeShow(sfWebRequest $request){
    $this->zonename = $request->getParameter('name');
    $this->forward404Unless($this->zonename);
    
    $c = new Criteria();
    $c->add(ZonesPeer::NAME, $this->zonename);
    
    $this->bosses = BossesPeer::doSelectJoinAll($c);
  }
}
