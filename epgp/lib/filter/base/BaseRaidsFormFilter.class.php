<?php

require_once(sfConfig::get('sf_lib_dir').'/filter/base/BaseFormFilterPropel.class.php');

/**
 * Raids filter form base class.
 *
 * @package    epgp
 * @subpackage filter
 * @author     Your name here
 * @version    SVN: $Id: sfPropelFormFilterGeneratedTemplate.php 15484 2009-02-13 13:13:51Z fabien $
 */
class BaseRaidsFormFilter extends BaseFormFilterPropel
{
  public function setup()
  {
    $this->setWidgets(array(
      'note'           => new sfWidgetFormFilterInput(),
      'boss'           => new sfWidgetFormPropelChoice(array('model' => 'Bosses', 'add_empty' => true)),
      'baseval'        => new sfWidgetFormFilterInput(),
      'inflatedval'    => new sfWidgetFormFilterInput(),
      'date'           => new sfWidgetFormFilterDate(array('from_date' => new sfWidgetFormDate(), 'to_date' => new sfWidgetFormDate(), 'with_empty' => false)),
      'attendees_list' => new sfWidgetFormPropelChoice(array('model' => 'Roster', 'add_empty' => true)),
    ));

    $this->setValidators(array(
      'note'           => new sfValidatorPass(array('required' => false)),
      'boss'           => new sfValidatorPropelChoice(array('required' => false, 'model' => 'Bosses', 'column' => 'id')),
      'baseval'        => new sfValidatorSchemaFilter('text', new sfValidatorNumber(array('required' => false))),
      'inflatedval'    => new sfValidatorSchemaFilter('text', new sfValidatorNumber(array('required' => false))),
      'date'           => new sfValidatorDateRange(array('required' => false, 'from_date' => new sfValidatorDate(array('required' => false)), 'to_date' => new sfValidatorDate(array('required' => false)))),
      'attendees_list' => new sfValidatorPropelChoice(array('model' => 'Roster', 'required' => false)),
    ));

    $this->widgetSchema->setNameFormat('raids_filters[%s]');

    $this->errorSchema = new sfValidatorErrorSchema($this->validatorSchema);

    parent::setup();
  }

  public function addAttendeesListColumnCriteria(Criteria $criteria, $field, $values)
  {
    if (!is_array($values))
    {
      $values = array($values);
    }

    if (!count($values))
    {
      return;
    }

    $criteria->addJoin(AttendeesPeer::RAIDS_ID, RaidsPeer::ID);

    $value = array_pop($values);
    $criterion = $criteria->getNewCriterion(AttendeesPeer::ROSTER_ID, $value);

    foreach ($values as $value)
    {
      $criterion->addOr($criteria->getNewCriterion(AttendeesPeer::ROSTER_ID, $value));
    }

    $criteria->add($criterion);
  }

  public function getModelName()
  {
    return 'Raids';
  }

  public function getFields()
  {
    return array(
      'id'             => 'Number',
      'note'           => 'Text',
      'boss'           => 'ForeignKey',
      'baseval'        => 'Number',
      'inflatedval'    => 'Number',
      'date'           => 'Date',
      'attendees_list' => 'ManyKey',
    );
  }
}
